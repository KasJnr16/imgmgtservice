package com.pm.billingservice.repository;

import com.pm.billingservice.model.Billing;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository
        extends JpaRepository<Billing, UUID> {

    List<Billing> findByBillingAccount_PatientId(UUID patientId);

    List<Billing> findByBillingAccount_PatientIdAndPaidFalse(UUID patientId);
}
