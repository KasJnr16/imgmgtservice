package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.Report;
import com.pm.analyticsservice.model.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByPatientIdOrderByGeneratedAtDesc(UUID patientId);
    List<Report> findByPatientIdAndReportType(UUID patientId, ReportType reportType);
}