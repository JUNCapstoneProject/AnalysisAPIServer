package com.AnalysisAPIserver.domain.auth.dto;

import lombok.Data;

/**
 * Client ID 요청 DTO이다.
 */
@Data
public final class ClientIdRequest {

    /**
     * 이메일.
     */
    private String email;
}
