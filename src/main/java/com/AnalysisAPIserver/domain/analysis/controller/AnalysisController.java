package com.AnalysisAPIserver.domain.analysis.controller;

import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponse;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResultRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisStatusResponse;
import com.AnalysisAPIserver.domain.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/request")
    public ResponseEntity<AnalysisResponse> requestAnalysis(@RequestBody AnalysisRequest request) {
        return ResponseEntity.ok(analysisService.routeRequest(request));
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<AnalysisStatusResponse> getAnalysisStatus(@PathVariable String requestId) {
        return ResponseEntity.ok(analysisService.getAnalysisStatus(requestId));
    }

    @PostMapping("/result")
    public ResponseEntity<String> saveAnalysisResult(@RequestBody AnalysisResultRequest request) {
        analysisService.saveAnalysisResult(request);
        return ResponseEntity.ok("분석 결과 저장 완료");
    }
}
