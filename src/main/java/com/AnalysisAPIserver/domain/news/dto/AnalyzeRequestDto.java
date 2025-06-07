package com.AnalysisAPIserver.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * API 요청 데이터를 담는 최상위 클래스입니다.
 *
 * @param <T> 요청 본문에 포함될 특정 아이템의 타입.
 */
@Getter
public final class AnalyzeRequestDto<T> {

    /**
     * 요청의 헤더 정보를 담고 있습니다.
     */
    private Header header;

    /**
     * 요청의 본문 정보를 담고 있습니다.
     */
    private Body<T> body;

    /**
     * 요청 헤더의 세부 정보를 정의하는 내부 클래스입니다.
     */
    @Getter
    public static final class Header {

        /**
         * 요청이 전송된 호스트 정보입니다.
         */
        @JsonProperty("Host")
        private String host;

        /**
         * 요청의 출처(Origin) 정보입니다.
         */
        @JsonProperty("Origin")
        private String origin;

        /**
         * 이전 페이지(Referer) 정보입니다.
         */
        @JsonProperty("Referer")
        private String referer;

        /**
         * 실제 요청이 보내진 전체 URL입니다.
         */
        @JsonProperty("RequestURL")
        private String requestURL;

        /**
         * 사용된 HTTP 메소드 (예: "GET", "POST")입니다.
         */
        @JsonProperty("RequestMethod")
        private String requestMethod;

        /**
         * 인증 토큰 정보입니다.
         */
        @JsonProperty("Authorization")
        private String authorization;
    }

    /**
     * 요청 본문의 세부 정보를 정의하는 내부 클래스입니다.
     *
     * @param <T> 분석할 실제 아이템의 타입.
     */
    @Getter
    public static final class Body<T> {

        /**
         * API 사용자의 클라이언트 ID입니다.
         */
        @JsonProperty("client_id")
        private String clientId;

        /**
         * API 사용자의 클라이언트 시크릿입니다.
         */
        @JsonProperty("client_secret")
        private String clientSecret;

        /**
         * 분석을 요청할 실제 데이터 아이템입니다.
         */
        private T item;
    }
}
