package com.AnalysisAPIserver.domain.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 분석 결과 DTO이다.
 * (StockAnalysisAPI가 AnalysisAPIserver /api/analysis/result 로 보내는 요청 본문)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResultDto {
    /**
     * 응답 ID.
     */
    @JsonProperty("response_id")
    private String responseId;
    /**
     * 상태 코드.
     */
    @JsonProperty("status_code")
    private Integer statusCode;
    /**
     * 메시지.
     */
    private String message;
    /**
     * 결과 항목.
     */
    private ResultItem item;

    /**
     * 분석 결과 항목 DTO이다.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultItem {
        /**
         * 이벤트 타입.
         */
        @JsonProperty("event_type")
        private String eventType;
        /**
         * 결과 내용.
         */
        private String result;
    }
}
