package com.AnalysisAPIserver.domain.auth.dto;

import lombok.Data;

/**
 * 회원가입 요청 DTO이다.
 */
@Data
public final class RegisterRequest {

    /**
     * 사용자 이메일.
     */
    private String email;

    /**
     * 사용자 비밀번호.
     */
    private String password;
}
