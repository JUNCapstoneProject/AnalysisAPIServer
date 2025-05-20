package com.AnalysisAPIserver.domain.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 클라이언트 생성 성공 시 응답으로 사용되는 DTO입니다.
 * 생성된 클라이언트의 ID와 앱 이름을 포함합니다.
 */
@Getter
@AllArgsConstructor
public class ClientCreateResponse {
    /**
     * 새로 생성된 클라이언트의 고유 ID입니다.
     */
    private String clientId;
    /**
     * 생성된 클라이언트(애플리케이션)의 이름입니다.
     */
    private String appName;
}
