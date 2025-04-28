package com.AnalysisAPIserver.domain.client.dto;

import com.AnalysisAPIserver.domain.client.entity.Client;
import lombok.Getter;

/**
 * 클라이언트 조회 응답 DTO이다.
 */
@Getter
public final class ClientResponse {

    /**
     * 클라이언트 ID.
     */
    private final String clientId;

    /**
     * 클라이언트 이름.
     */
    private final String name;

    /**
     * 생성자.
     *
     * @param client 클라이언트 엔티티
     */
    public ClientResponse(final Client client) {
        this.clientId = client.getClientId();
        this.name = client.getName();
    }
}
