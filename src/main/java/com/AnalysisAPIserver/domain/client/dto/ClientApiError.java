package com.AnalysisAPIserver.domain.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 에러 발생 시 응답 정보를 담는 DTO입니다.
 */
@Getter
@AllArgsConstructor
public class ClientApiError {
    /**
     * HTTP 상태 코드 또는 커스텀 에러 코드입니다.
     */
    private int code;
    /**
     * 에러 메시지입니다. (보통 에러 코드의 이름)
     */
    private String message;
    /**
     * 에러에 대한 상세 설명입니다.
     */
    private String description;
}
