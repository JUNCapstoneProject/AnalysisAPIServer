package com.AnalysisAPIserver.domain.logs.controller;


import com.AnalysisAPIserver.domain.logs.dto.ApiLogRequest;
import com.AnalysisAPIserver.domain.logs.dto.ApiLogResponse;
import com.AnalysisAPIserver.domain.logs.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @PostMapping("")
    public ResponseEntity<String> saveApiLog(@RequestBody ApiLogRequest request) {
        logService.saveApiLog(request);
        return ResponseEntity.ok("API 사용 기록 저장 완료");
    }

    @GetMapping("")
    public ResponseEntity<ApiLogResponse> getDeveloperLogs(@RequestParam String clientId) {
        return ResponseEntity.ok(logService.getDeveloperLogs(clientId));
    }
}
