package com.AnalysisAPIserver.domain.analysis.dto;



import com.fasterxml.jackson.annotation.JsonProperty; // Import for JsonProperty
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 분석 요청 DTO이다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisRequestDto {
    /**
     * 요청 헤더 정보.
     */
    private Map<String, String> header;
    /**
     * 요청 본문 아이템.
     */
    private AnalysisItemBody body;

    /**
     * 분석 요청 DTO의 body에 해당하는 클래스이다.
     * news_item 또는 finance_item을 포함한다.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalysisItemBody {
        /**
         * 클라이언트 ID.
         */
        @JsonProperty("client_id")
        private String clientId;
        /**
         * 클라이언트 시크릿.
         */
        @JsonProperty("client_secret")
        private String clientSecret;
        /**
         * 분석 대상 아이템 (news_item 또는 finance_item).
         */
        private Map<String, Object> item;
    }
}
