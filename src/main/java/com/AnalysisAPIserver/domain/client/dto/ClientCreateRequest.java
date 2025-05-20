package com.AnalysisAPIserver.domain.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
/**
 * 클라이언트 생성 요청 시 사용되는 DTO입니다.
 * 개발자 ID, 앱 이름, 앱 카테고리 ID, API 카테고리 ID, 콜백 URL을 포함합니다.
 */
@Getter
@AllArgsConstructor
public class ClientCreateRequest {
    /**
     * 클라이언트를 생성하는 개발자의 ID입니다. 필수 항목입니다.
     */
    @NotNull(message = "개발자 ID는 필수입니다.")
    private Long developerId;

    /**
     * 생성할 클라이언트(애플리케이션)의 이름입니다. 필수 항목이며 공백일 수 없습니다.
     */
    @NotBlank(message = "앱 이름은 필수입니다.")
    private String appName;

    /**
     * 클라이언트(애플리케이션)의 카테고리 ID입니다. 필수 항목입니다.
     */
    @NotNull(message = "앱 카테고리 ID는 필수입니다.")
    private Long appCategoryId;

    /**
     * 클라이언트가 사용할 API의 카테고리 ID입니다. 필수 항목입니다.
     */
    @NotNull(message = "API 카테고리 ID는 필수입니다.")
    private Long apiCategoryId;

    /**
     * 클라이언트의 콜백 URL입니다. OAuth2 인증 등에 사용됩니다. 필수 항목이며 공백일 수 없습니다.
     */
    @NotBlank(message = "Callback URL은 필수입니다.")
    private String callbackUrl;
}
