package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private int budgetId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "amount")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", message = "Amount must be a positive value")
    private BigDecimal amount;

    @Column(name = "budget_date")
    private LocalDateTime budgetDate;

    public Budget() {
    }

    public Budget(Customer customer, BigDecimal amount, LocalDateTime budgetDate) {
        this.customer = customer;
        this.amount = amount;
        this.budgetDate = budgetDate;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getBudgetDate() {
        return budgetDate;
    }

    public void setBudgetDate(LocalDateTime budgetDate) {
        this.budgetDate = budgetDate;
    }
}
