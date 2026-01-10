package com.pm.billingservice.controller;

import com.pm.billingservice.dto.CreateBillingRequestDTO;
import com.pm.billingservice.model.Billing;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.service.BillingAccountService;
import com.pm.billingservice.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;
    private final BillingAccountService billingAccountService;

    public BillingController(
        BillingService billingService,
        BillingAccountService billingAccountService
    ) {
        this.billingService = billingService;
        this.billingAccountService = billingAccountService;
    }

    /* ===================== BILLING ACCOUNT ===================== */

    @PostMapping("/accounts/{patientId}")
    @Operation(summary = "Create a billing account for a patient")
    public ResponseEntity<?> createAccount(
        @PathVariable UUID patientId,
        @RequestParam String accountName
    ) {
        try {
            BillingAccount account =
                billingAccountService.createAccount(patientId, accountName);

            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create billing account");
        }
    }

    @GetMapping("/accounts/{patientId}")
    @Operation(summary = "Get billing account by patient ID")
    public ResponseEntity<?> getAccount(
        @PathVariable UUID patientId
    ) {
        try {
            BillingAccount account =
                billingAccountService.getAccountByPatientId(patientId);

            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /* ===================== BILLINGS ===================== */

    @PostMapping("/charges")
    @Operation(summary = "Create a billing charge for a patient")
    public ResponseEntity<?> createBilling(
        @Valid @RequestBody CreateBillingRequestDTO request
    ) {
        try {
            Billing billing = billingService.createBilling(
                request.getPatientId(),
                request.getAmount(),
                request.getDueDate()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(billing);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create billing charge");
        }
    }

    @GetMapping("/charges/patient/{patientId}")
    @Operation(summary = "Get all billing records for a patient")
    public ResponseEntity<?> getPatientBillings(
        @PathVariable UUID patientId
    ) {
        try {
            List<Billing> billings =
                billingService.getBillingsForPatient(patientId);

            return ResponseEntity.ok(billings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to retrieve billings");
        }
    }

    @GetMapping("/charges/patient/{patientId}/outstanding")
    @Operation(summary = "Get outstanding (unpaid) bills for a patient")
    public ResponseEntity<?> getOutstandingBills(
        @PathVariable UUID patientId
    ) {
        try {
            return ResponseEntity.ok(
                billingService.getOutstandingBills(patientId)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to retrieve outstanding bills");
        }
    }

    @PostMapping("/charges/{billingId}/pay")
    @Operation(summary = "Pay a billing charge")
    public ResponseEntity<?> payBill(
        @PathVariable UUID billingId
    ) {
        try {
            Billing billing = billingService.payBill(billingId);
            return ResponseEntity.ok(billing);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/accounts/{patientId}/settled")
    @Operation(summary = "Check if a patient's billing account is fully settled")
    public ResponseEntity<?> isAccountSettled(
        @PathVariable UUID patientId
    ) {
        try {
            boolean settled = billingService.isAccountSettled(patientId);
            return ResponseEntity.ok(settled);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to check account settlement status");
        }
    }
}
