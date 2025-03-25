package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Budget;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    public Budget findByBudgetId(int budgetId);

    public List<Budget> findByCustomerCustomerId(Integer customerId);

    @Query("SELECT SUM(b.amount) FROM Budget b WHERE b.customer.id = :customerId")
    public BigDecimal sumAmountByCustomerCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT SUM(b.amount) FROM Budget b")
    public BigDecimal getSumAmount();

    @Query("SELECT b.customer, SUM(b.amount) as totalBudget " +
       "FROM Budget b " +
       "GROUP BY b.customer " +
       "ORDER BY totalBudget DESC")
    List<Object[]> findTopCustomersByBudgetSum();



}
