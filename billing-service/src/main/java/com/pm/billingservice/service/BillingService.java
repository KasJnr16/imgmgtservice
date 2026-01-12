package com.pm.billingservice.service;

import com.pm.billingservice.model.Billing;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.repository.BillingAccountRepository;
import com.pm.billingservice.repository.BillingRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final BillingAccountRepository billingAccountRepository;

    public BillingService(
        BillingRepository billingRepository,
        BillingAccountRepository billingAccountRepository
    ) {
        this.billingRepository = billingRepository;
        this.billingAccountRepository = billingAccountRepository;
    }

    // 1️⃣ Create a billing charge
    @Transactional
    public Billing createBilling(
        UUID patientId,
        BigDecimal amount,
        LocalDateTime dueDate
    ) {
        return createBilling(patientId, amount, dueDate, "Medical Service");
    }

    public Billing createBilling(
        UUID patientId,
        BigDecimal amount,
        LocalDateTime dueDate,
        String description
    ) {
        BillingAccount account = billingAccountRepository.findByPatientId(patientId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Billing account not found for patient: " + patientId));

        Billing billing = new Billing();
        billing.setBillingAccount(account);
        billing.setAmountDue(amount);
        billing.setDueDate(dueDate);
        billing.setPaid(false);
        billing.setDescription(description);

        return billingRepository.save(billing);
    }

    // 2️⃣ Get all billings for a patient
    public List<Billing> getBillingsForPatient(UUID patientId) {
        return billingRepository.findByBillingAccount_PatientId(patientId);
    }

    // 3️⃣ Get unpaid bills
    public List<Billing> getOutstandingBills(UUID patientId) {
        return billingRepository.findByBillingAccount_PatientIdAndPaidFalse(patientId);
    }

    // 4️⃣ Pay a bill
    @Transactional
    public Billing payBill(UUID billingId) {
        Billing billing = billingRepository.findById(billingId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Billing record not found: " + billingId));

        if (billing.isPaid()) {
            throw new IllegalStateException("Billing already paid");
        }

        billing.setPaid(true);
        return billingRepository.save(billing);
    }

    // 5️⃣ Check if patient is fully settled
    public boolean isAccountSettled(UUID patientId) {
        return billingRepository
            .findByBillingAccount_PatientIdAndPaidFalse(patientId)
            .isEmpty();
    }
}
