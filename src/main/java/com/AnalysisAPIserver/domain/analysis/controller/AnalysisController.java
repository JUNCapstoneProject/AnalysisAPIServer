package com.AnalysisAPIserver.domain.analysis.controller;

import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponse;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResultRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisStatusResponse;
import com.AnalysisAPIserver.domain.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 분석 요청을 처리하는 컨트롤러이다.
 */
@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public final class AnalysisController {

    /**
     * 분석 서비스.
     */
    private final AnalysisService analysisService;

    /**
     * 분석 요청을 라우팅한다.
     *
     * @param request 분석 요청
     * @return 분석 결과
     */
    @PostMapping("/request")
    public ResponseEntity<AnalysisResponse> requestAnalysis(
            @RequestBody final AnalysisRequest request) {
        return ResponseEntity.ok(this.analysisService.routeRequest(request));
    }

    /**
     * 분석 상태를 조회한다.
     *
     * @param requestId 요청 ID
     * @return 분석 상태
     */
    @GetMapping("/status/{requestId}")
    public ResponseEntity<AnalysisStatusResponse> getAnalysisStatus(
            @PathVariable final String requestId) {
        return ResponseEntity.ok(
                this.analysisService.getAnalysisStatus(requestId));
    }

    /**
     * 분석 결과를 저장한다.
     *
     * @param request 분석 결과 저장 요청
     * @return 성공 메시지
     */
    @PostMapping("/result")
    public ResponseEntity<String> saveAnalysisResult(
            @RequestBody final AnalysisResultRequest request) {
        this.analysisService.saveAnalysisResult(request);
        return ResponseEntity.ok("분석 결과 저장 완료");
    }
}
