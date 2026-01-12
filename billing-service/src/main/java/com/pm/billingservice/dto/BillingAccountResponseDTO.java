package com.pm.billingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BillingAccountResponseDTO {
    private UUID id;
    private UUID patientId;
    private String accountName;
    private BigDecimal balance;
    private boolean isSettled;
    private LocalDateTime createdAt;

    // Constructors
    public BillingAccountResponseDTO() {}

    public BillingAccountResponseDTO(UUID id, UUID patientId, String accountName, 
                                   BigDecimal balance, boolean isSettled, LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.accountName = accountName;
        this.balance = balance;
        this.isSettled = isSettled;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
