package com.AnalysisAPIserver.domain.client.exception;

import com.AnalysisAPIserver.domain.client.dto.ClientApiError;
import com.AnalysisAPIserver.domain.client.dto.ClientApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * com.AnalysisAPIserver.domain.client 패키지 내에서 발생하는 예외를 처리하는 핸들러입니다.
 * {@link RestControllerAdvice}를 사용하여 전역적으로 예외를 관리합니다.
 */
@RestControllerAdvice(basePackages = "com.AnalysisAPIserver.domain.client")
public final class ClientExceptionHandler {

    /**
     * HTTP 404 Not Found 상태 코드 값.
     */
    private static final int NOT_FOUND_STATUS_CODE
            = HttpStatus.NOT_FOUND.value();
    /**
     * HTTP 400 Bad Request 상태 코드 값.
     */
    private static final int BAD_REQUEST_STATUS_CODE
            = HttpStatus.BAD_REQUEST.value();
    /**
     * HTTP 500 Internal Server Error 상태 코드 값.
     */
    private static final int INTERNAL_SERVER_ERROR_CODE
            = HttpStatus.INTERNAL_SERVER_ERROR.value();

    /**
     * {@link ClientException} 발생 시 처리합니다.
     * 에러 코드에 따라 적절한 HTTP 상태 코드와 메시지를 설정하여 응답합니다.
     *
     * @param ex 발생한 ClientException 객체.
     * @return 에러 정보를 담은 ResponseEntity 객체.
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ClientApiResponse<Object>> handleClientException(
            final ClientException ex) {
        ClientErrorCode code = ex.getErrorCode();
        ClientApiError error;

        switch (code) {
            case OWNER_NOT_FOUND, CATEGORY_NOT_FOUND:
                error = new ClientApiError(NOT_FOUND_STATUS_CODE,
                        "Developer or category not found.",
                        code.getMessage());
                break;
            case CLIENT_NOT_FOUND, APPLICATION_NOT_FOUND:
                error = new ClientApiError(NOT_FOUND_STATUS_CODE,
                        "Client or application not found.",
                        code.getMessage());
                break;
            default:
                error = new ClientApiError(INTERNAL_SERVER_ERROR_CODE,
                        "Unexpected error.",
                        "Unhandled exception code");
                break;
        }

        return ResponseEntity.status(error.getCode())
                .body(ClientApiResponse.error(error));
    }

    /**
     * {@link MethodArgumentNotValidException}
     * 발생 시 (주로 @Valid 어노테이션 검증 실패) 처리합니다.
     * HTTP 400 (Bad Request) 상태 코드와 유효성 검증 실패 메시지를 응답합니다.
     *
     * @param ex 발생한 MethodArgumentNotValidException 객체.
     * @return 에러 정보를 담은 ResponseEntity 객체.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientApiResponse<Object>> handleValidationException(
            final MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldError().getDefaultMessage();
        ClientApiError error = new ClientApiError(BAD_REQUEST_STATUS_CODE,
                "Invalid request.", message);
        return ResponseEntity.badRequest().body(ClientApiResponse.error(error));
    }

    /**
     * 위에서 처리되지 않은 모든 {@link Exception}을 처리합니다.
     * HTTP 500 (Internal Server Error) 상태 코드와 예외 메시지를 응답합니다.
     *
     * @param ex 발생한 Exception 객체.
     * @return 에러 정보를 담은 ResponseEntity 객체.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientApiResponse<Object>>
    handleGeneralException(final Exception ex) {

        ClientApiError error
                = new ClientApiError(INTERNAL_SERVER_ERROR_CODE,
                "Internal Server Error.",
                ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClientApiResponse.error(error));
    }
}
