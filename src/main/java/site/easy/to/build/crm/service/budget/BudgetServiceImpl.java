package site.easy.to.build.crm.service.budget;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.BudgetRepository;

@Service
public class BudgetServiceImpl implements BudgetService{

    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> findByCustomerId(Integer customerId){
        return budgetRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public BigDecimal sumAmountByCustomerId( Integer customerId){
        return budgetRepository.sumAmountByCustomerCustomerId(customerId);
    }

    @Override
    public BigDecimal getSumAmount(){
        return budgetRepository.getSumAmount();
    }

    @Override
    public List<Map<String, Object>> getTop3CustomersByBudgetSum() {
        List<Object[]> topCustomers = budgetRepository.findTopCustomersByBudgetSum().stream()
                .limit(3)
                .collect(Collectors.toList());
        
        // Transformation des résultats en Map avec Customer Name et Total Budget
        return topCustomers.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            Customer customer = (Customer) result[0];  // Le premier élément est le client
            BigDecimal totalBudget = (BigDecimal) result[1];  // Le second est le budget total
            map.put("customer", customer);  // Suppose que Customer a un champ "name"
            map.put("totalBudget", totalBudget);
            return map;
        }).collect(Collectors.toList());
    }
    
}
