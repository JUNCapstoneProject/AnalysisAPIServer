package com.AnalysisAPIserver.domain.analysis.exception;

/**
 * 분석 관련 예외 클래스이다.
 */
public final class AnalysisException extends RuntimeException {

    /**
     * AnalysisException 생성자.
     *
     * @param message 에러 메시지
     */
    public AnalysisException(final String message) {
        super(message);
    }
}
