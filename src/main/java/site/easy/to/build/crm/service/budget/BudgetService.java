package site.easy.to.build.crm.service.budget;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;


public interface BudgetService {

    public Budget save(Budget budget);

    public List<Budget> findAll();

    public List<Budget> findByCustomerId(Integer customerId);

    public BigDecimal sumAmountByCustomerId( Integer customerId);

    public BigDecimal getSumAmount();

    public List<Map<String, Object>> getTop3CustomersByBudgetSum();
    
}
