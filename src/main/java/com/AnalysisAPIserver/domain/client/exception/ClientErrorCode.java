package com.AnalysisAPIserver.domain.client.exception;


import lombok.Getter;

/**
 * 클라이언트 관련 작업 시 발생할 수 있는 에러 코드를 정의한 열거형입니다.
 * 각 에러 코드는 고유한 메시지를 가집니다.
 */
@Getter
public enum ClientErrorCode {
    /**
     * 요청된 개발자 정보를 찾을 수 없을 때 발생하는 에러 코드입니다.
     */
    OWNER_NOT_FOUND("개발자를 찾을 수 없습니다."),
    /**
     * 요청된 앱 카테고리 정보를 찾을 수 없을 때 발생하는 에러 코드입니다.
     */
    CATEGORY_NOT_FOUND("앱 카테고리를 찾을 수 없습니다."),
    /**
     * 요청된 클라이언트(애플리케이션) 정보를 찾을 수 없을 때 발생하는 에러 코드입니다.
     */
    CLIENT_NOT_FOUND("클라이언트를 찾을 수 없습니다."),
    /**
     * 요청된 애플리케이션 정보를 찾을 수 없을 때 발생하는 에러 코드입니다.
     * (CLIENT_NOT_FOUND와 유사하나, 내부적인 구분이나 다른 컨텍스트에서 사용될 수 있습니다.)
     */
    APPLICATION_NOT_FOUND("앱 정보를 찾을 수 없습니다.");

    /**
     * 에러 코드에 해당하는 설명 메시지입니다.
     */
    private final String message;

    /**
     * ClientErrorCode 열거형 생성자입니다.
     *
     * @param msg 에러 코드에 대한 설명 메시지.
     */
    ClientErrorCode(final String msg) {
        this.message = msg;
    }

    /**
     * 에러 코드에 해당하는 설명 메시지를 반환합니다.
     *
     * @return 에러 메시지 문자열.
     */
    public String getMessage() {
        return this.message;
    }
}
