
package com.pm.analyticsservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class PatientEventConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(PatientEventConsumer.class);
    private final ObjectMapper objectMapper;
    
    public PatientEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @KafkaListener(topics = "patient-events", groupId = "analytics-group")
    public void consumePatientEvent(byte[] message) {
        try {
            String jsonMessage = new String(message);
            Map<String, Object> event = objectMapper.readValue(jsonMessage, Map.class);
            
            String eventType = (String) event.get("eventType");
            log.info("Received patient event: {}", eventType);
            
            // Process different event types
            switch (eventType) {
                case "PATIENT_CREATED":
                    handlePatientCreated(event);
                    break;
                case "PATIENT_UPDATED":
                    handlePatientUpdated(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing patient event", e);
        }
    }
    
    private void handlePatientCreated(Map<String, Object> event) {
        log.info("Processing patient creation: {}", event.get("patientId"));
        // Could initialize analytics data for new patient
    }
    
    private void handlePatientUpdated(Map<String, Object> event) {
        log.info("Processing patient update: {}", event.get("patientId"));
        // Could update cached patient data
    }
}
