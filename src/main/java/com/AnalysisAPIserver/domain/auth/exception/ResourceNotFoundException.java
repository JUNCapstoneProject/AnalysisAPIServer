package com.AnalysisAPIserver.domain.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 요청한 리소스를 찾을 수 없을 때 발생하는 예외입니다.
 * 이 예외는 HTTP 404 Not Found 상태 코드로 매핑됩니다.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 지정된 상세 메시지로 새로운 ResourceNotFoundException을 생성합니다.
     *
     * @param message 상세 메시지.
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * 지정된 상세 메시지와 원인으로 새로운 ResourceNotFoundException을 생성합니다.
     *
     * @param message 상세 메시지.
     * @param cause   원인이 되는 예외.
     */
    public
    ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
