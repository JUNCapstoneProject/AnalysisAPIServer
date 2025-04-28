package com.AnalysisAPIserver.domain.client.dto;

import lombok.Getter;

/**
 * 클라이언트 생성 요청 DTO이다.
 */
@Getter
public final class ClientCreateRequest {

    /**
     * 개발자 ID.
     */
    private Long developerId;

    /**
     * 클라이언트 이름.
     */
    private String name;
}
