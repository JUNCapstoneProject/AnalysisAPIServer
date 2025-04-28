package com.AnalysisAPIserver.domain.client.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 클라이언트 에러 코드 모음이다.
 */
@Getter
public enum ClientErrorCode {

    /**
     * 클라이언트를 찾을 수 없음 에러.
     */
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "클라이언트를 찾을 수 없습니다."),

    /**
     * 서버 내부 오류 에러.
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

    ClientErrorCode(final HttpStatus statusParam, final String messageParam) {
        this.status = statusParam;
        this.message = messageParam;
    }
}
