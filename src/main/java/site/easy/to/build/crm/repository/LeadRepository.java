package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {
    public Lead findByLeadId(int id);

    public List<Lead> findByCustomerCustomerId(int customerId);
    public List<Lead> findByManagerId(int userId);

    public List<Lead> findByEmployeeId(int userId);

    Lead findByMeetingId(String meetingId);

    public List<Lead> findByEmployeeIdOrderByCreatedAtDesc(int employeeId, Pageable pageable);

    public List<Lead> findByManagerIdOrderByCreatedAtDesc(int managerId, Pageable pageable);

    public List<Lead> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);
    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);

    @Query("SELECT SUM(e.amount) FROM Expense e where e.lead.id is not null")
    public BigDecimal getSumAmount();

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.customer.id = :customerId and e.lead.id is not null")
    public BigDecimal sumAmountByCustomerId(@Param("customerId") Integer customerId);

}
