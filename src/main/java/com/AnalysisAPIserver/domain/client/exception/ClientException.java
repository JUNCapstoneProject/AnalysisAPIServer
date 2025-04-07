package com.AnalysisAPIserver.domain.client.exception;



import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ClientErrorCode errorCode;

    public ClientException(ClientErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
