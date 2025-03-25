package site.easy.to.build.crm.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Rate;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.rate.RateService;
import site.easy.to.build.crm.service.ticket.TicketService;

@RestController
@Controller
@RequestMapping("/api/rest")
public class DashboardController {

    private final CustomerService customerService;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final RateService rateService;

    @Autowired
    public DashboardController(CustomerService customerService, TicketService ticketService, LeadService leadService,
            ExpenseService expenseService, BudgetService budgetService, RateService rateService) {
        this.customerService = customerService;
        this.ticketService= ticketService;
        this.leadService= leadService;
        this.expenseService= expenseService;
        this.budgetService= budgetService;
        this.rateService= rateService;
    }

    @GetMapping("/customer")
    public List<Customer> getAllCustomers(){
        return customerService.findAll();
    }

    @GetMapping("/customer/count")
    public long getCountCustomer(){
        return customerService.count();
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable("id") Integer id){
        return customerService.findByCustomerId(id);
    }

    @GetMapping("/ticket")
    public List<Ticket> getAllTickets(){
        return ticketService.findAll();
    }

    @GetMapping("/ticket/count")
    public long getCountTicket(){
        return ticketService.count();
    }

    @GetMapping("/ticket/customer/{customerId}")
    public List<Ticket> getAllTicketByCustomerId(@PathVariable("customerId") Integer customerId){
        return ticketService.findCustomerTickets(customerId);
    }

    @PostMapping("/ticket/update/{ticketId}/{amount}")
    public Expense modifyTicket(@PathVariable Integer ticketId, @PathVariable BigDecimal amount) {
        return expenseService.updateByTicketId(ticketId, amount);
    }

    @GetMapping("/ticket/delete/{ticketId}")
    public void deleteTicket(@PathVariable Integer ticketId){
        expenseService.deleteByTicketId(ticketId);
        Ticket ticket= ticketService.findByTicketId(ticketId);
        ticketService.delete(ticket);
    }

    @GetMapping("/ticket/sum")
    public BigDecimal getTicketSumAmount(){
        return ticketService.getSumAmount();
    }

    @GetMapping("/ticket/sum/customer/{customerId}")
    public BigDecimal getTicketSumAmountByCustomerId(@PathVariable Integer customerId){
        return ticketService.sumAmountByCustomerId(customerId);
    }

    @GetMapping("/ticket/{ticketId}/expense")
    public Expense getExpenseByTicketId(@PathVariable Integer ticketId){
        return expenseService.findByTicketId(ticketId);
    }

    @GetMapping("/lead")
    public List<Lead> getAllLeads(){
        return leadService.findAll();
    }

    @GetMapping("/lead/count")
    public long getCountLead(){
        List<Lead> leads= leadService.findAll();
        return leads.size();
    }

    @GetMapping("/lead/customer/{customerId}")
    public List<Lead> getAllLeadByCustomerId(@PathVariable("customerId") Integer customerId){
        return leadService.getCustomerLeads(customerId);
    }

    @PostMapping("/lead/update/{leadId}/{amount}")
    public Expense modifyLead(@PathVariable Integer leadId, @PathVariable BigDecimal amount) {
        return expenseService.updateByLeadId(leadId, amount);
    }

    @GetMapping("/lead/delete/{leadId}")
    public void deletelead(@PathVariable Integer leadId){
        expenseService.deleteByLeadId(leadId);
        Lead lead= leadService.findByLeadId(leadId);
        leadService.delete(lead);
    }

    @GetMapping("/lead/sum")
    public BigDecimal getLeadSumAmount(){
        return leadService.getSumAmount();
    }

    @GetMapping("/lead/sum/customer/{customerId}")
    public BigDecimal getLeadSumAmountByCustomerId(@PathVariable Integer customerId){
        return leadService.sumAmountByCustomerId(customerId);
    }

    @GetMapping("/lead/{leadId}/expense")
    public Expense getExpenseByLeadId(@PathVariable Integer leadId){
        return expenseService.findByLeadId(leadId);
    }

    @GetMapping("/expense")
    public List<Expense> getAllExpenses(){
        return expenseService.findAll();
    }


    @GetMapping("/budget/sum")
    public BigDecimal getBudgetSumAmount(){
        return budgetService.getSumAmount();
    }

    @GetMapping("/budget/sum/customer/{customerId}")
    public BigDecimal getBudgetSumAmountByCustomerId(@PathVariable Integer customerId){
        return budgetService.sumAmountByCustomerId(customerId);
    }

    @GetMapping("/budget/customer/top3")
    public ResponseEntity<List<Map<String, Object>>> getTop3Customers() {
        List<Map<String, Object>> topCustomers = budgetService.getTop3CustomersByBudgetSum();
        return new ResponseEntity<>(topCustomers, HttpStatus.OK);
    }

    @PostMapping("/rate/{rateValue}")
    public Rate saveRate(@PathVariable BigDecimal rateValue) {
        return rateService.save(rateValue);
    }

    @GetMapping("/rate")
    public Rate getLast(){
        return rateService.getLast();
    }


    





    









}
