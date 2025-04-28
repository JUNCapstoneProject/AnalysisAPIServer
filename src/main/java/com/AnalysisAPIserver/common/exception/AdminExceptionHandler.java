package com.AnalysisAPIserver.common.exception;

import com.AnalysisAPIserver.domain.admin.exception.AdminErrorCode;
import com.AnalysisAPIserver.domain.admin.exception.AdminException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 관리자의 예외를 처리하는 핸들러 클래스이다.
 */
@RestControllerAdvice
public final class AdminExceptionHandler {

    /**
     * AdminException이 발생했을 때 처리한다.
     *
     * @param e 발생한 AdminException
     * @return 상태 코드와 메시지를 담은 응답
     */
    @ExceptionHandler(AdminException.class)
    public ResponseEntity<String> handleAdminException(final AdminException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(e.getErrorCode().getMessage());
    }

    /**
     * 일반 Exception이 발생했을 때 처리한다.
     *
     * @param e 발생한 Exception
     * @return 상태 코드와 메시지를 담은 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(final Exception e) {
        return ResponseEntity.status(
                        AdminErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(AdminErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
