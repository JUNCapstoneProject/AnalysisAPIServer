package com.AnalysisAPIserver.domain.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends AuthException {
    public UnauthorizedException(String message) {
        super(message);
    }
}