package com.AnalysisAPIserver.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 인증되지 않은 접근 예외이다.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public final class UnauthorizedException extends AuthException {

    /**
     * 생성자.
     *
     * @param message 예외 메시지
     */
    public UnauthorizedException(final String message) {
        super(message);
    }
}
