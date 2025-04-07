package com.AnalysisAPIserver.domain.admin.controller;


import com.AnalysisAPIserver.domain.admin.dto.ApiUsageResponse;
import com.AnalysisAPIserver.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/api-usage")
    public ResponseEntity<ApiUsageResponse> getApiUsageStats() {
        return ResponseEntity.ok(adminService.getApiUsageStats());
    }
}
