package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.entity.Expense;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    public Expense findByExpenseId(int expenseId);

    public List<Expense> findByCustomerCustomerId(Integer customerId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.customer.id = :customerId")
    public BigDecimal sumAmountByCustomerId(@Param("customerId") Integer customerId);

    public Expense findByTicketTicketId(Integer ticketId);

    public Expense findByLeadLeadId(Integer leadId);



}
