package site.easy.to.build.crm.service.expense;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService{

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findByCustomerId(Integer customerId){
        return expenseRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public BigDecimal sumAmountByCustomerId(@Param("customerId") Integer customerId){
        return expenseRepository.sumAmountByCustomerId(customerId);
    }

    @Override
    public Expense findByTicketId(Integer ticketId){
        return expenseRepository.findByTicketTicketId(ticketId);
    }

    @Override
    public Expense findByLeadId(Integer LeadId){
        return expenseRepository.findByLeadLeadId(LeadId);
    }

    @Override
    @Transactional
    public Expense updateByTicketId(Integer ticketId, BigDecimal amount) {
        Expense expense = expenseRepository.findByTicketTicketId(ticketId);
        expense.setAmount(amount);
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public Expense updateByLeadId(Integer leadId, BigDecimal amount) {
        Expense expense = expenseRepository.findByLeadLeadId(leadId);
        expense.setAmount(amount);
        return expenseRepository.save(expense);
    }
    
    @Override
    public void deleteByLeadId(Integer leadId){
        Expense expense= expenseRepository.findByLeadLeadId(leadId);
        expenseRepository.delete(expense);
    }

    @Override
    public void deleteByTicketId(Integer ticketId){
        Expense expense= expenseRepository.findByTicketTicketId(ticketId);
        expenseRepository.delete(expense);
    }
    
}
