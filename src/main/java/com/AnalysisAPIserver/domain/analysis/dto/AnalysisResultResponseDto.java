package com.AnalysisAPIserver.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 분석 결과 전달 API 응답 DTO이다.
 * 이 DTO는 AnalysisAPIserver가 StockAnalysisAPI에게 보내는 응답입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResultResponseDto {

    /**
     * 원본 요청의 ID (원래 AnalysisResultDto의 response_id에 해당).
     */
    private String requestId;

    /**
     * 크롤링 서버로의 전달 상태 메시지.
     */
    private String status;
}
