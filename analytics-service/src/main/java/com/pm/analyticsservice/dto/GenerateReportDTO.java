package com.pm.analyticsservice.dto;

import com.pm.analyticsservice.model.ReportFormat;
import com.pm.analyticsservice.model.ReportType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GenerateReportDTO {
    private UUID patientId;
    private UUID generatedByStaffId;
    private ReportType reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ReportFormat format;
}