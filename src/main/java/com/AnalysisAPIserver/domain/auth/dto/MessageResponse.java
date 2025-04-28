package com.AnalysisAPIserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 단순 메시지 응답 DTO이다.
 */
@Getter
@AllArgsConstructor
public final class MessageResponse {

    /**
     * 응답 메시지.
     */
    private String message;
}
