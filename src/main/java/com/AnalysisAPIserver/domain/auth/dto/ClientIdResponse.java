package com.AnalysisAPIserver.domain.auth.dto;

/**
 * Client ID 응답 DTO이다.
 */
public final class ClientIdResponse {

    /**
     * 클라이언트 ID.
     */
    private final String clientId;

    /**
     * 클라이언트 시크릿.
     */
    private final String clientSecret;

    /**
     * ClientIdResponse 생성자.
     *
     * @param clientIdParam 클라이언트 ID
     * @param clientSecretParam 클라이언트 시크릿
     */
    public ClientIdResponse(final String clientIdParam,
                            final String clientSecretParam) {
        this.clientId = clientIdParam;
        this.clientSecret = clientSecretParam;
    }

    /**
     * 클라이언트 ID를 반환한다.
     *
     * @return 클라이언트 ID
     */
    public String getClientId() {
        return this.clientId;
    }

    /**
     * 클라이언트 시크릿을 반환한다.
     *
     * @return 클라이언트 시크릿
     */
    public String getClientSecret() {
        return this.clientSecret;
    }
}
