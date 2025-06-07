package com.AnalysisAPIserver.domain.finance.controller;

import com.AnalysisAPIserver.domain.finance.dto.AnalyzeRequestDto;
import com.AnalysisAPIserver.domain.finance.dto.AnalyzeResponseDto;
import com.AnalysisAPIserver.domain.finance.dto.FinanceItemDto;
import com.AnalysisAPIserver.domain.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 재무제표 분석 API의 엔드포인트를 정의합니다.
 * 이 컨트롤러는 상속하여 사용할 필요가 없으므로 final로 선언하여 확장성을 제한합니다.
 */
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public final class FinanceController {

    /**
     * 재무 분석 비즈니스 로직을 처리하는 서비스입니다.
     */
    private final FinanceService financeService;

    /**
     * 재무 분석을 요청받아 처리하고 그 결과를 반환합니다.
     *
     * @param requestDto 클라이언트로부터 받은 요청 데이터.
     * @return 분석 결과가 담긴 ResponseEntity 객체.
     */
    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponseDto<Object>> analyzeFinance(
            @RequestBody final AnalyzeRequestDto<FinanceItemDto> requestDto) {

        final AnalyzeResponseDto<Object> responseDto =
                financeService.analyzeViaInternalWebSocket(requestDto);

        return ResponseEntity
                // ⭐️ 오류 수정: getStatus_code() -> getStatusCode()
                .status(responseDto.getStatusCode())
                .body(responseDto);
    }
}
