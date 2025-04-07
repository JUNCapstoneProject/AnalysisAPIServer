package com.AnalysisAPIserver.domain.client.exception;



import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientErrorCode {

    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "클라이언트를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생");

    private final HttpStatus status;
    private final String message;

    ClientErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
