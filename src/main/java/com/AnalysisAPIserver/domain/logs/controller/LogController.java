package com.AnalysisAPIserver.domain.logs.controller;

import com.AnalysisAPIserver.domain.logs.dto.ApiLogRequest;
import com.AnalysisAPIserver.domain.logs.dto.ApiLogResponse;
import com.AnalysisAPIserver.domain.logs.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그 관련 컨트롤러이다.
 */
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public final class LogController {

    /**
     * 로그 서비스.
     */
    private final LogService logService;

    /**
     * API 사용 기록을 저장한다.
     *
     * @param request API 로그 요청
     * @return 성공 메시지
     */
    @PostMapping
    public ResponseEntity<String> saveApiLog(
            @RequestBody final ApiLogRequest request) {
        this.logService.saveApiLog(request);
        return ResponseEntity.ok("API 사용 기록 저장 완료");
    }

    /**
     * 클라이언트 ID로 API 사용 기록을 조회한다.
     *
     * @param clientId 클라이언트 ID
     * @return API 로그 응답
     */
    @GetMapping
    public ResponseEntity<ApiLogResponse> getDeveloperLogs(
            @RequestParam final String clientId) {
        return ResponseEntity.ok(this.logService.getDeveloperLogs(clientId));
    }
}
