package com.AnalysisAPIserver.domain.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * API 요청 데이터를 담는 최상위 클래스입니다.
 * 외부로부터 JSON 데이터를 받아야 하므로 변경 가능하도록 Setter를 추가합니다.
 *
 * @param <T> 요청 본문에 포함될 특정 아이템의 타입.
 */
@Getter
@Setter
@NoArgsConstructor
public class AnalyzeRequestDto<T> {

    /**
     * 요청 헤더 정보.
     */
    private Header header;
    /**
     * 요청 본문 데이터.
     */
    private Body<T> body;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Header {
        /**
         * 요청 호스트.
         */
        @JsonProperty("Host")
        private String host;

        /**
         * 요청 출처.
         */
        @JsonProperty("Origin")
        private String origin;

        /**
         * 요청 리퍼러.
         */
        @JsonProperty("Referer")
        private String referer;

        /**
         * 요청 URL.
         */
        @JsonProperty("Request URL")
        private String requestURL;

        /**
         * 요청 HTTP 메소드.
         */
        @JsonProperty("RequestMethod")
        private String requestMethod;

        /**
         * 인증 정보.
         */
        @JsonProperty("Authorization")
        private String authorization;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Body<T> {
        /**
         * 클라이언트 아이디.
         */
        @JsonProperty("client_id")
        private String clientId;

        /**
         * 클라이언트 시크릿.
         */
        @JsonProperty("client_secret")
        private String clientSecret;

        /**
         * 실제 요청 데이터 아이템.
         */
        private T item;
    }
}

