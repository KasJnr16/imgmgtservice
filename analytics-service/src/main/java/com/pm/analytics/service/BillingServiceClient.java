package com.pm.analytics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BillingServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${billing.service.url:http://localhost:8083}")
    private String billingServiceUrl;

    public String createScanCharge(String patientId, String scanAppointmentId, String scanType, Double amount) {
        String url = billingServiceUrl + "/api/billing/scan-charge";
        
        ScanChargeRequest request = new ScanChargeRequest(
            patientId,
            scanAppointmentId,
            scanType,
            amount,
            "Medical scan: " + scanType + " for appointment " + scanAppointmentId
        );

        try {
            BillingChargeResponse response = restTemplate.postForObject(url, request, BillingChargeResponse.class);
            return response != null ? response.getChargeId() : null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create billing charge: " + e.getMessage(), e);
        }
    }

    // Inner classes for request/response
    public static class ScanChargeRequest {
        private String patientId;
        private String scanAppointmentId;
        private String scanType;
        private Double amount;
        private String description;

        public ScanChargeRequest() {}

        public ScanChargeRequest(String patientId, String scanAppointmentId, String scanType, Double amount, String description) {
            this.patientId = patientId;
            this.scanAppointmentId = scanAppointmentId;
            this.scanType = scanType;
            this.amount = amount;
            this.description = description;
        }

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

    public static class BillingChargeResponse {
        private String chargeId;

        public String getChargeId() { return chargeId; }
        public void setChargeId(String chargeId) { this.chargeId = chargeId; }
    }
}
