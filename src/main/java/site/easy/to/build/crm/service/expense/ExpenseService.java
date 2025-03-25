package site.easy.to.build.crm.service.expense;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.query.Param;

import site.easy.to.build.crm.entity.Expense;


public interface ExpenseService {

    public Expense save(Expense Expense);

    public List<Expense> findAll();

    public List<Expense> findByCustomerId(Integer customerId);

    public BigDecimal sumAmountByCustomerId(Integer customerId);

    public Expense findByTicketId(Integer ticketId);

    public Expense findByLeadId(Integer leadId);

    public Expense updateByTicketId(Integer ticketId, BigDecimal amount);

    public Expense updateByLeadId(Integer leadId, BigDecimal amount);

    public void deleteByLeadId(Integer leadId);
    public void deleteByTicketId(Integer TicketId);
}
