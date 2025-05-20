package com.AnalysisAPIserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 개발자 등록 응답 DTO.
 */
@Getter
@AllArgsConstructor
public class AuthRegisterResponse {

    /**
     * 요청 성공 여부.
     */
    private boolean success;

    /**
     * 응답 본문.
     */
    private ResponseBody response;

    /**
     * 오류 메시지.
     */
    private String error;

    /**
     * 응답 본문.
     */
    @Getter
    @AllArgsConstructor
    public static class ResponseBody {

        /**
         * 사용자 정보.
         */
        private User user;
    }

    /**
     * 사용자 정보 DTO.
     */
    @Getter
    @AllArgsConstructor
    public static class User {

        /**
         * 개발자 ID.
         */
        private long developerId;
    }
}
