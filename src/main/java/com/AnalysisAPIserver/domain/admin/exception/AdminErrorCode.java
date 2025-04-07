package com.AnalysisAPIserver.domain.admin.exception;



import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminErrorCode {

    API_USAGE_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "API 사용량 데이터를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생");

    private final HttpStatus status;
    private final String message;

    AdminErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
