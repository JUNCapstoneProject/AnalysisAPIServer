package com.AnalysisAPIserver.domain.admin.exception;

import lombok.Getter;

/**
 * 관리자 예외 클래스이다.
 */
@Getter
public final class AdminException extends RuntimeException {

    /**
     * 에러 코드.
     */
    private final AdminErrorCode errorCode;

    /**
     * AdminException 생성자.
     *
     * @param errorCodeParam 에러 코드
     */
    public AdminException(final AdminErrorCode errorCodeParam) {
        super(errorCodeParam.getMessage());
        this.errorCode = errorCodeParam;
    }
}
