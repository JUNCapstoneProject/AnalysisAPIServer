package com.AnalysisAPIserver.domain.admin.controller;

import com.AnalysisAPIserver.domain.admin.dto.ApiUsageResponse;
import com.AnalysisAPIserver.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 API 사용량 조회하는 컨트롤러이다.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public final class AdminController {

    /**
     * 관리자 서비스.
     */
    private final AdminService adminService;

    /**
     * 전체 API 사용량 통계 조회.
     *
     * @return API 사용량 응답
     */
    @GetMapping("/api-usage")
    public ResponseEntity<ApiUsageResponse> getApiUsageStats() {
        return ResponseEntity.ok(this.adminService.getApiUsageStats());
    }
}
