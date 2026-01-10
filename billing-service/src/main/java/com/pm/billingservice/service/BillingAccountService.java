package com.pm.billingservice.service;

import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.repository.BillingAccountRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BillingAccountService {

    private final BillingAccountRepository billingAccountRepository;

    public BillingAccountService(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

    public BillingAccount createAccount(UUID patientId, String accountName) {

        billingAccountRepository.findByPatientId(patientId)
            .ifPresent(account -> {
                throw new IllegalStateException(
                    "Billing account already exists for patient: " + patientId);
            });

        BillingAccount account = new BillingAccount();
        account.setPatientId(patientId);
        account.setAccountName(accountName);
        account.setCreatedAt(LocalDateTime.now());

        return billingAccountRepository.save(account);
    }

    public BillingAccount getAccountByPatientId(UUID patientId) {
        return billingAccountRepository.findByPatientId(patientId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Billing account not found for patient: " + patientId));
    }
}
