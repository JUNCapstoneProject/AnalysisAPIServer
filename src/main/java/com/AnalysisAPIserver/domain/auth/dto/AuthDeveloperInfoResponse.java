package com.AnalysisAPIserver.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


/**
 * 개발자 정보 조회 응답 DTO.
 */
@Getter
@Builder
@AllArgsConstructor
public class AuthDeveloperInfoResponse {

    /**
     * json User.
     */
    private User user;


    /**
     * 내부 유저 정보 DTO.
     */
    @Getter
    @AllArgsConstructor
    public static class User {
        /**
         * 개발자 ID.
         */
        private String userName;

        /**
         * 이메일.
         */
        private String userType;

        /**
         * 사용자명.
         */
        private LocalDate createdAt;
    }


}
