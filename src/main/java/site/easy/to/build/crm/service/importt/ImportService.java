package site.easy.to.build.crm.service.importt;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class ImportService {

    private final JdbcTemplate jdbcTemplate;

    public ImportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void importEmployees(MultipartFile file) throws IOException, CsvValidationException {
        try {
            System.out.println("Mamomboka");
            createTempTable();
            List<EmployeeCsvDto> employees = parseCsv(file);
            insertIntoTempTable(employees);
            validateData();
            insertIntoEmployeeTable();
            System.out.println("Vita import");
        } finally {
            cleanUpTempTable();
        }
    }

    private void createTempTable() {
        System.out.println("Creation table temporaire");
        jdbcTemplate.execute("CREATE TEMPORARY TABLE employeeTemp ("
                + "username VARCHAR(45) NOT NULL,"
                + "first_name VARCHAR(45) NOT NULL,"
                + "last_name VARCHAR(45) NOT NULL,"
                + "email VARCHAR(45) NOT NULL,"
                + "password VARCHAR(80) NOT NULL,"
                + "provider VARCHAR(45))");
        System.out.println("Table temporaire creer");
    }

    private List<EmployeeCsvDto> parseCsv(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<EmployeeCsvDto> csvToBean = new CsvToBeanBuilder<EmployeeCsvDto>(reader)
                    .withType(EmployeeCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }

    private void insertIntoTempTable(List<EmployeeCsvDto> employees) {
        System.out.println("Insertion dans table temp");
        String sql = "INSERT INTO employeeTemp (username, first_name, last_name, email, password, provider) VALUES (?, ?, ?, ?, ?, ?)";
        
        try{
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    EmployeeCsvDto employee = employees.get(i);
                    System.out.println(employee.getUsername()+""+employee.getFirstName()+""+employee.getLastName()+""+employee.getEmail()+""+employee.getPassword()+""+employee.getProvider());
                    
                    ps.setString(1, employee.getUsername());
                    ps.setString(2, employee.getFirstName());
                    ps.setString(3, employee.getLastName());
                    ps.setString(4, employee.getEmail());
                    ps.setString(5, employee.getPassword());
                    ps.setString(6, employee.getProvider());
                }
    
                @Override
                public int getBatchSize() {
                    return employees.size();
                }
            });
        }
        catch(Exception e){ 
            System.out.println("Erreur insertion donnees: "+ e.getMessage());
        }
        System.out.println("Insertion dans table temp terminee");

    }

    private void validateData() {
        System.out.println("Validation data");
        validateRequiredFields();
        validateUniqueEmails();
        validateEmailUniquenessInDatabase();
        System.out.println("Validation terminee");
    }

    private void validateRequiredFields() {
        String sql = "SELECT COUNT(*) FROM employeeTemp "
                + "WHERE username = '' OR first_name = '' OR last_name = '' OR email = '' OR password = ''";
        
        int invalidCount = jdbcTemplate.queryForObject(sql, Integer.class);
        if (invalidCount > 0) {
            System.out.println("Certains champs obligatoires sont vides");
            throw new RuntimeException("Certains champs obligatoires sont vides");
        }
    }

    private void validateUniqueEmails() {
        String sql = "SELECT COUNT(*) FROM (SELECT email FROM employeeTemp GROUP BY email HAVING COUNT(*) > 1) duplicates";
        
        int duplicateCount = jdbcTemplate.queryForObject(sql, Integer.class);
        if (duplicateCount > 0) {
            throw new RuntimeException("Emails en double dans le fichier CSV");
        }
    }

    private void validateEmailUniquenessInDatabase() {
        String sql = "SELECT COUNT(e.id) FROM employee e "
                + "INNER JOIN employeeTemp et ON e.email = et.email";
        
        int existingCount = jdbcTemplate.queryForObject(sql, Integer.class);
        if (existingCount > 0) {
            throw new RuntimeException("Certains emails existent déjà dans la base");
        }
    }

    private void insertIntoEmployeeTable() {
        String sql = "INSERT INTO employee (username, first_name, last_name, email, password, provider) "
                + "SELECT username, first_name, last_name, email, password, provider FROM employeeTemp";
        
        jdbcTemplate.execute(sql);
    }

    private void cleanUpTempTable() {
        jdbcTemplate.execute("DROP TEMPORARY TABLE IF EXISTS employeeTemp");
    }

    // Classe DTO pour la lecture CSV
    public static class EmployeeCsvDto {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String provider;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getProvider() {
            return provider;
        }
        public void setProvider(String provider) {
            this.provider = provider;
        }

        // Getters et Setters
    }
}