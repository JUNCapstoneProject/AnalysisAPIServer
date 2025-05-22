package com.AnalysisAPIserver.domain.client.exception;

import lombok.Getter;

/**
 * 클라이언트 관련 로직 처리 중 발생하는 사용자 정의 예외 클래스입니다.
 * {@link ClientErrorCode}를 통해 구체적인 에러 상황을 전달합니다.
 */
@Getter
public final class ClientException extends RuntimeException {

    /**
     * 발생한 예외에 해당하는 {@link ClientErrorCode}입니다.
     */
    private final ClientErrorCode errorCode;

    /**
     * ClientException 생성자입니다.
     *
     * @param errorCodeParam 발생한 에러에 해당하는 {@link ClientErrorCode}
     */
    public ClientException(final ClientErrorCode errorCodeParam) {
        super(errorCodeParam.getMessage());
        this.errorCode = errorCodeParam;
    }
}
