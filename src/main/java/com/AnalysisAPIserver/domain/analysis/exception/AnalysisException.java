package com.AnalysisAPIserver.domain.analysis.exception;

/**
 * 분석 관련 예외이다. // Example Javadoc for the class, adjust as needed
 */
public class AnalysisException extends RuntimeException {

    /**
     * 생성자.
     *
     * @param message 예외 메시지
     */
    public AnalysisException(final String message) {
        super(message);
    }

    /**
     * 생성자.
     *
     * @param message 예외 메시지
     * @param cause   원인 예외
     */
    public AnalysisException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
