package com.pm.analytics.dto;

import com.pm.analytics.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public class ScanUpdateDTO {
    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    private String notes;
    private String radiologistId;

    // For image upload
    private String imageFilePath;

    // Getters and Setters
    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRadiologistId() {
        return radiologistId;
    }

    public void setRadiologistId(String radiologistId) {
        this.radiologistId = radiologistId;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
