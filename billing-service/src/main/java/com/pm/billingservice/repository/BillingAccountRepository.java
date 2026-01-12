package com.pm.billingservice.repository;

import com.pm.billingservice.model.BillingAccount;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface BillingAccountRepository
        extends JpaRepository<BillingAccount, UUID> {

    @EntityGraph(attributePaths = {"billings"})
    Optional<BillingAccount> findByPatientId(UUID patientId);
}
