package com.AnalysisAPIserver.domain.client.dto;

import lombok.Getter;

/**
 * 클라이언트 관련 작업 (예: 이름 변경) 후 응답으로 사용되는 DTO입니다.
 * 클라이언트 ID와 앱 이름을 포함합니다.
 */
@Getter
public class ClientResponse {
    /**
     * 클라이언트의 고유 ID입니다.
     */
    private String clientId;
    /**
     * 클라이언트(애플리케이션)의 이름입니다.
     */
    private String appName;

    /**
     * ClientResponse 객체를 생성합니다.
     *
     * @param clientIdParam 클라이언트 ID
     * @param appNameParam  애플리케이션 이름
     */
    public
    ClientResponse(final String clientIdParam, final String appNameParam) {
        this.clientId = clientIdParam;
        this.appName = appNameParam;
    }
}
