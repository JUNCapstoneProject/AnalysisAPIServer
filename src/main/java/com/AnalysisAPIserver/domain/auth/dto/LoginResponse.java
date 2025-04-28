package com.AnalysisAPIserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 로그인 응답 DTO이다.
 */
@Data
@AllArgsConstructor
public final class LoginResponse {

    /**
     * 액세스 토큰.
     */
    private String accessToken;
}
