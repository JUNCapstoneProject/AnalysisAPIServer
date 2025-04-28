package com.AnalysisAPIserver.domain.client.exception;

import lombok.Getter;

/**
 * 클라이언트 예외 클래스이다.
 */
@Getter
public final class ClientException extends RuntimeException {

    /**
     * 에러 코드.
     */
    private final ClientErrorCode errorCode;

    /**
     * ClientException 생성자.
     *
     * @param errorCodeParam 에러 코드
     */
    public ClientException(final ClientErrorCode errorCodeParam) {
        super(errorCodeParam.getMessage());
        this.errorCode = errorCodeParam;
    }
}
