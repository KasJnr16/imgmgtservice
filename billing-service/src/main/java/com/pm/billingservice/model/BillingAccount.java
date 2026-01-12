package com.pm.billingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BillingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID patientId;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Computed fields (not stored in database)
    @Transient
    private BigDecimal balance = BigDecimal.ZERO;

    @Transient
    private boolean isSettled = false;

    @OneToMany(mappedBy = "billingAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"billingAccount"})
    private List<Billing> billings = new ArrayList<>();  // ← Fixed: Changed from BillingAccount to Billing

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters & Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Billing> getBillings() {
        return billings;
    }

    public void setBillings(List<Billing> billings) {
        this.billings = billings;
        // Update computed fields when billings change
        updateBalanceAndSettledStatus();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }

    // Helper method to compute balance and settled status
    private void updateBalanceAndSettledStatus() {
        if (billings != null) {
            BigDecimal totalDue = billings.stream()
                .filter(b -> !b.isPaid())
                .map(Billing::getAmountDue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.balance = totalDue;
            this.isSettled = billings.stream().allMatch(Billing::isPaid);
        } else {
            this.balance = BigDecimal.ZERO;
            this.isSettled = true;
        }
    }

    @PostLoad
    protected void onLoad() {
        updateBalanceAndSettledStatus();
    }
}