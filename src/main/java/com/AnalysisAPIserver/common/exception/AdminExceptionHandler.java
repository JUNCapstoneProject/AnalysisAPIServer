package com.AnalysisAPIserver.common.exception;


import com.AnalysisAPIserver.domain.admin.exception.AdminException;
import com.AnalysisAPIserver.domain.admin.exception.AdminErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<String> handleAdminException(AdminException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(e.getErrorCode().getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(AdminErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(AdminErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
