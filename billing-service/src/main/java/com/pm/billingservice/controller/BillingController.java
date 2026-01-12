package com.pm.billingservice.controller;

import com.pm.billingservice.dto.BillingAccountResponseDTO;
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
            BillingAccountResponseDTO account =
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
                request.getDueDate(),
                request.getDescription()
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

    @PostMapping("/scan-charge")
    @Operation(summary = "Create a billing charge for medical scan")
    public ResponseEntity<?> createScanCharge(@RequestBody ScanChargeRequest request) {
        try {
            // Create billing charge for scan
            CreateBillingRequestDTO billingRequest = new CreateBillingRequestDTO();
            billingRequest.setPatientId(UUID.fromString(request.getPatientId()));
            billingRequest.setAmount(java.math.BigDecimal.valueOf(request.getAmount()));
            billingRequest.setDueDate(java.time.LocalDateTime.now().plusDays(30));
            billingRequest.setDescription(request.getDescription());

            Billing billing = billingService.createBilling(
                billingRequest.getPatientId(),
                billingRequest.getAmount(),
                billingRequest.getDueDate(),
                billingRequest.getDescription()
            );
            
            // Return charge ID
            return ResponseEntity.ok(new ScanChargeResponse(billing.getId().toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create scan charge: " + e.getMessage());
        }
    }
}

// DTOs for scan charge
class ScanChargeRequest {
    private String patientId;
    private String scanAppointmentId;
    private String scanType;
    private Double amount;
    private String description;

    // Getters and setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getScanAppointmentId() { return scanAppointmentId; }
    public void setScanAppointmentId(String scanAppointmentId) { this.scanAppointmentId = scanAppointmentId; }

    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

class ScanChargeResponse {
    private String chargeId;

    public ScanChargeResponse(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeId() { return chargeId; }
    public void setChargeId(String chargeId) { this.chargeId = chargeId; }
}
