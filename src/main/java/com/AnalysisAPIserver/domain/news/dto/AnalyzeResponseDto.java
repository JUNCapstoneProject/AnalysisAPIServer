package com.AnalysisAPIserver.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * API 응답 데이터를 담는 최상위 클래스입니다.
 *
 * @param <T> 응답 본문에 포함될 특정 아이템의 타입.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class AnalyzeResponseDto<T> {

    /**
     * 응답의 고유 식별자입니다.
     */
    @JsonProperty("response_id")
    private final String responseId;

    /**
     * HTTP 상태 코드입니다.
     */
    @JsonProperty("status_code")
    private final Integer statusCode;

    /**
     * 응답 결과에 대한 메시지입니다.
     */
    private final String message;

    /**
     * 응답의 실제 데이터 아이템입니다.
     */
    private final T item;

    /**
     * 응답 아이템의 세부 구조를 정의하는 내부 클래스입니다.
     *
     * @param <T> 실제 결과 데이터의 타입.
     */
    @Getter
    @Builder
    public static final class ResponseItem<T> {

        /**
         * 이벤트의 종류 (예: "news", "finance") 입니다.
         */
        @JsonProperty("event_type")
        private final String eventType;

        /**
         * 처리된 실제 결과 데이터입니다.
         */
        private final T result;
    }
}
