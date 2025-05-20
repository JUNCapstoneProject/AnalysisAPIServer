package com.AnalysisAPIserver.domain.admin.controller;


import com.AnalysisAPIserver.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 API 사용량 조회를 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public final class AdminController {

    /**
     * 관리자 서비스.
     */
    private final AdminService adminService;

    /*
     * 전체 API 사용량 통계 조회 엔드포인트입니다.
     * 추후 확장 시 필터링 조건 등 파라미터가 추가될 수 있습니다.
     *
     * @return API 사용량 응답
     *
     * @GetMapping("/api-usage")
     * public ResponseEntity<ApiUsageResponse> getApiUsageStats() {
     *     return ResponseEntity.ok(this.adminService.getApiUsageStats());
     * }
     */
}
