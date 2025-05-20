package com.AnalysisAPIserver.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * 개발자 등록 요청 DTO.
 */
@Getter
@Setter
public class AuthRegisterRequest {

    /**
     * 접근 토큰.
     */
    private String accessToken;

    /**
     * 사용자 이름.
     */
    private String userName;
}
