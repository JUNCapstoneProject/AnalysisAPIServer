package com.AnalysisAPIserver.domain.client.dto;



import com.AnalysisAPIserver.domain.client.entity.Client;
import lombok.Getter;

@Getter
public class ClientResponse {
    private String clientId;
    private String name;

    public ClientResponse(Client client) {
        this.clientId = client.getClientId();
        this.name = client.getName();
    }
}
