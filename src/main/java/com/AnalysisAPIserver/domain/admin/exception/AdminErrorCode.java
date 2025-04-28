package com.AnalysisAPIserver.domain.admin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 관리자 에러 코드 모음이다.
 */
@Getter
public enum AdminErrorCode {

    /**
     * API 사용량 데이터 없음.
     */
    API_USAGE_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "API 사용량 데이터를 찾을 수 없습니다."),

    /**
     * 서버 내부 오류.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생");

    /**
     * HTTP 상태 코드.
     */
    private final HttpStatus status;

    /**
     * 에러 메시지.
     */
    private final String message;

    AdminErrorCode(final HttpStatus statusParam, final String messageParam) {
        this.status = statusParam;
        this.message = messageParam;
    }
}
