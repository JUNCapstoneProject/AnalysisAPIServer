package com.AnalysisAPIserver.domain.logs.exception;

/**
 * 로그 관련 예외 클래스이다.
 */
public final class LogException extends RuntimeException {

    /**
     * 생성자.
     *
     * @param message 예외 메시지.
     */
    public LogException(final String message) {
        super(message);
    }
}
