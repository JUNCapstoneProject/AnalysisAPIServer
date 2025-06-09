package com.AnalysisAPIserver.domain.finance.exception;

import lombok.Getter;

/**
 * API 관련 예외 상황을 나타내는 클래스입니다.
 * {@link ErrorCode}를 포함하여 어떤 종류의 에러가 발생했는지 명확히 합니다.
 * 이 클래스는 상속하여 사용하지 않으므로 final로 선언합니다.
 */
@Getter
public final class ApiException extends RuntimeException {

    /**
     * 발생한 에러의 종류를 정의하는 에러 코드입니다.
     */
    private final ErrorCode errorCode;

    /**
     * 지정된 에러 코드로 ApiException을 생성합니다.
     *
     * @param pErrorCode 발생한 에러의 상세 정보를 담고 있는 ErrorCode enum. (파라미터 이름 변경)
     */
    public ApiException(final ErrorCode pErrorCode) {
        super(pErrorCode.getMessage());
        this.errorCode = pErrorCode; // 필드에 파라미터 값을 할당
    }
}
