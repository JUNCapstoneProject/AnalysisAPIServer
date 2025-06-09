package com.AnalysisAPIserver.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * API 응답 데이터를 담는 최상위 클래스입니다.
 *
 * @param <T> 응답 본문에 포함될 특정 아이템의 타입.
 */
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class AnalyzeResponseDto<T> {

    /**
     * 고유한 응답 ID.
     */
    private final String responseId;
    /**
     * HTTP 상태 코드.
     */
    private final Integer statusCode;
    /**
     * 응답 메시지.
     */
    private final String message;
    /**
     * 응답 데이터 아이템.
     */
    private final T item;

    /**
     * Jackson이 JSON 데이터를 이 DTO 객체로 변환(역직렬화)할 때 사용할 생성자입니다.
     *
     * @param pResponseId  고유한 응답 ID
     * @param pStatusCode  HTTP 상태 코드
     * @param pMessage     응답 메시지
     * @param pItem        응답 데이터 아이템
     */
    @JsonCreator
    public AnalyzeResponseDto(
            @JsonProperty("response_id") final String pResponseId,
            @JsonProperty("status_code") final Integer pStatusCode,
            @JsonProperty("message") final String pMessage,
            @JsonProperty("item") final T pItem) {
        this.responseId = pResponseId;
        this.statusCode = pStatusCode;
        this.message = pMessage;
        this.item = pItem;
    }

    /**
     * 응답 아이템의 세부 구조를 정의하는 내부 클래스입니다.
     *
     * @param <T> 결과 데이터의 타입.
     */
    @Getter
    @Builder
    @ToString
    public static final class ResponseItem<T> {
        /**
         * 이벤트 타입.
         */
        private final String eventType;
        /**
         * 처리 결과 데이터.
         */
        private final T result;

        /**
         * Jackson이 JSON 데이터를 ResponseItem 객체로 변환할 때 사용할 생성자입니다.
         *
         * @param pEventType 이벤트 타입
         * @param pResult    처리 결과 데이터
         */
        @JsonCreator
        public ResponseItem(
                @JsonProperty("event_type") final String pEventType,
                @JsonProperty("result") final T pResult) {
            this.eventType = pEventType;
            this.result = pResult;
        }
    }
}
