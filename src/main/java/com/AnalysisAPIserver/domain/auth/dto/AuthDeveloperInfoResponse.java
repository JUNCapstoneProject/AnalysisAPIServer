package com.AnalysisAPIserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter; // @Data 대신 @Getter만 사용 권장 (불변성)

import java.time.LocalDate;

/**
 * 개발자 정보 조회 응답 DTO.
 */
@Getter
@Builder
@AllArgsConstructor // 모든 필드를 받는 생성자
public class AuthDeveloperInfoResponse {

    /**
     * 사용자 정보.
     */
    private User user;

    /**
     * 내부 사용자 정보 DTO.
     */
    @Getter
    @AllArgsConstructor // 모든 필드를 받는 생성자
    public static class User {
        /**
         * 사용자 이름.
         */
        private String userName;

        /**
         * 사용자 유형.
         */
        private String userType;

        /**
         * 계정 생성일.
         */
        private LocalDate createdAt;

        /**
         * 개발자 ID. (DB에서 조회 후 응답에 포함)
         */
        private Long developerId; // developerId 필드 추가
    }
}
