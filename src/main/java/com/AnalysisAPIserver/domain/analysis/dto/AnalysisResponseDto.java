package com.AnalysisAPIserver.domain.analysis.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 분석 요청 응답 DTO이다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResponseDto {
    /**
     * 응답 ID.
     */
    @JsonProperty("response_id")
    private String responseId;
    /**
     * 상태 코드.
     */
    @JsonProperty("status_code")
    private int statusCodeVal;

    /**
     * 응답 메시지.
     */
    private String message;
    /**
     * 응답 아이템.
     */
    private ResponseItem item;

    /**
     * 분석 요청 응답 DTO의 아이템 클래스이다.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseItem {
        /**
         * 이벤트 타입.
         */
        @JsonProperty("event_type")
        private String eventType;
        /**
         * 분석 결과.
         */
        private Object result;
    }
}
