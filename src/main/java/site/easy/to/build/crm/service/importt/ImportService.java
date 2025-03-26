package site.easy.to.build.crm.service.importt;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CustomerImportService customerImportService;
    private final BudgetImportService budgetImportService;
    private final ExpenseImportService expenseImportService;

    public ImportService(JdbcTemplate jdbcTemplate, UserRepository userRepository, 
            CustomerRepository customerRepository, CustomerImportService customerImportService,
            BudgetImportService budgetImportService, ExpenseImportService expenseImportService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.customerImportService = customerImportService;
        this.budgetImportService = budgetImportService;
        this.expenseImportService = expenseImportService;
    }

    @Transactional
    public ImportResult importAll(MultipartFile customerFile, MultipartFile budgetFile, MultipartFile expenseFile) 
            throws IOException {
        try {
            System.out.println("Début de l'importation combinée");

            // Étape 1 : Parsing et création des tables temporaires
            List<CustomerImportService.CustomerCsvDto> customers = parseAndPrepareCustomers(customerFile);
            List<BudgetImportService.BudgetCsvDto> budgets = parseAndPrepareBudgets(budgetFile);
            List<ExpenseImportService.ExpenseCsvDto> expenses = parseAndPrepareExpenses(expenseFile);

            // Étape 2 : Insertion dans les tables temporaires
            insertCustomersToTemp(customers);
            insertBudgetsToTemp(budgets);
            insertExpensesToTemp(expenses);

            // Étape 3 : Validation complète des données
            validateAllData();
            updateCustomerIds();

            // Étape 4 : Si tout est valide, insertion dans les tables finales
            insertAllToFinalTables();

            System.out.println("Importation combinée terminée avec succès");
            return new ImportResult(true, "Importation combinée réussie", null);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            return new ImportResult(false, errorMessage, null);
        } finally {
            cleanUpAllTempTables();
        }
    }

    private List<CustomerImportService.CustomerCsvDto> parseAndPrepareCustomers(MultipartFile file) throws IOException {
        customerImportService.createTempCustomerTable();
        return customerImportService.parseCsv(file);
    }

    private List<BudgetImportService.BudgetCsvDto> parseAndPrepareBudgets(MultipartFile file) throws IOException {
        budgetImportService.createTempBudgetTable();
        return budgetImportService.parseCsv(file);
    }

    private List<ExpenseImportService.ExpenseCsvDto> parseAndPrepareExpenses(MultipartFile file) throws IOException {
        expenseImportService.createTempExpenseTable();
        return expenseImportService.parseCsv(file);
    }

    private void insertCustomersToTemp(List<CustomerImportService.CustomerCsvDto> customers) throws IOException {
        try {
            customerImportService.insertIntoTempTable(customers);
        } catch (Exception e) {
            throw new IOException("Erreur lors de l'insertion des customers dans la table temporaire", e);
        }
    }

    private void insertBudgetsToTemp(List<BudgetImportService.BudgetCsvDto> budgets) throws IOException {
        try {
            budgetImportService.insertIntoTempTable(budgets);
        } catch (Exception e) {
            throw new IOException("Erreur lors de l'insertion des budgets dans la table temporaire", e);
        }
    }

    private void insertExpensesToTemp(List<ExpenseImportService.ExpenseCsvDto> expenses) throws IOException {
        try {
            expenseImportService.insertIntoTempTable(expenses);
        } catch (Exception e) {
            throw new IOException("Erreur lors de l'insertion des expenses dans la table temporaire", e);
        }
    }

    private void validateAllData() {
        System.out.println("Validation de toutes les données");
        customerImportService.validateData();
        budgetImportService.validateData();
        expenseImportService.validateData();
        System.out.println("Toutes les données sont valides");
    }

    private void updateCustomerIds(){
        budgetImportService.updateBudgetCustomerIds();
        expenseImportService.updateExpenseCustomerIds();
    }

    private void insertAllToFinalTables() {
        System.out.println("Insertion dans toutes les tables finales");
        customerImportService.insertIntoCustomerTable();
        budgetImportService.insertIntoBudgetTable();
        expenseImportService.insertIntoFinalTables();
        System.out.println("Insertion dans les tables finales terminée");
    }

    private void cleanUpAllTempTables() {
        customerImportService.cleanUpTempTable();
        budgetImportService.cleanUpTempTable();
        expenseImportService.cleanUpTempTable();
    }

    public static class ImportResult {
        private final boolean success;
        private final String message;
        private final String errorLine;

        public ImportResult(boolean success, String message, String errorLine) {
            this.success = success;
            this.message = message;
            this.errorLine = errorLine;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getErrorLine() {
            return errorLine;
        }
    }
}