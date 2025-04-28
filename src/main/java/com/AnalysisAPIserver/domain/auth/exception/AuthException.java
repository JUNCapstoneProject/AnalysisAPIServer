package com.AnalysisAPIserver.domain.auth.exception;

/**
 * 인증 실패 예외이다.
 */
public class AuthException extends RuntimeException {

    /**
     * 생성자.
     *
     * @param message 예외 메시지
     */
    public AuthException(final String message) {
        super(message);
    }
}
