package com.pm.analyticsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedicalStaffService {

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, String> getStaffNames(List<String> staffIds) {
        // This would typically call a user service or medical staff service
        // For now, return a simple implementation
        return staffIds.stream()
            .collect(Collectors.toMap(
                id -> id,
                id -> "Staff " + id.substring(0, 8) // Simple placeholder
            ));
    }

    public String getStaffName(String staffId) {
        if (staffId == null) return null;
        Map<String, String> names = getStaffNames(List.of(staffId));
        return names.get(staffId);
    }
}
