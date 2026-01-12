package com.pm.analyticsservice.dto;

import com.pm.analyticsservice.model.ReportFormat;
import com.pm.analyticsservice.model.ReportType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReportResponseDTO {
    private UUID id;
    private UUID patientId;
    private ReportType reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reportContent;
    private ReportFormat format;
    private LocalDateTime generatedAt;
}