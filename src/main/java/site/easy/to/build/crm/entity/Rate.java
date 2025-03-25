package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private int rateId;

    @Column(name = "rate")
    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.00", message = "Rate must be a positive value")
    private BigDecimal rate;

    @Column(name = "inserted_at")
    private LocalDateTime insertedAt;

    public Rate() {
        this.insertedAt = LocalDateTime.now();
    }

    public Rate(BigDecimal rate) {
        this.rate = rate;
        this.insertedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDateTime getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(LocalDateTime insertedAt) {
        this.insertedAt = insertedAt;
    }
}
