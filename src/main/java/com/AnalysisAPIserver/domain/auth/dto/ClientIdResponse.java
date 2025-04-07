package com.AnalysisAPIserver.domain.auth.dto;


public class ClientIdResponse {
    private String clientId;
    private String clientSecret;

    public ClientIdResponse(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    // Getter/Setter 생략
}
