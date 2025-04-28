package com.AnalysisAPIserver.common.exception;

import com.AnalysisAPIserver.domain.auth.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 인증 관련 예외를 처리하는 핸들러 클래스이다.
 */
@RestControllerAdvice
public final class AuthGlobalExceptionHandler {

    /**
     * AuthException이 발생했을 때 처리한다.
     *
     * @param e 발생한 AuthException
     * @return 상태 코드와 메시지를 담은 응답
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(final AuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    /**
     * 일반 Exception이 발생했을 때 처리한다.
     *
     * @param e 발생한 Exception
     * @return 상태 코드와 메시지를 담은 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류 발생");
    }
}
