package com.AnalysisAPIserver.domain.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.databind.PropertyNamingStrategies;
// import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 클라이언트 목록 조회 시 각 클라이언트 정보를 담는 DTO입니다.
 * 대부분의 필드는 camelCase로, 생성일자만 snake_case('create_at')로 직렬화됩니다.
 */
@Getter
@Builder

public class ClientListResponseDto {

    /**
     * 클라이언트(애플리케이션)의 이름입니다. JSON 출력: "appName"
     */
    private String appName;

    /**
     * 클라이언트의 고유 ID입니다. JSON 출력: "clientId"
     */
    private String clientId;

    /**
     * 클라이언트가 사용하는 API의 카테고리 ID입니다. JSON 출력: "apiCategoryId"
     */
    private String apiCategoryId; // 응답 샘플은 "apiCategoryId" (camelCase)

    /**
     * 클라이언트(애플리케이션)의 카테고리 ID입니다. JSON 출력: "appCategoryId"
     */
    private String appCategoryId; // 응답 샘플은 "appCategoryId" (camelCase)

    /**
     * 클라이언트의 현재 상태입니다. JSON 출력: "status"
     */
    private String status;

    /**
     * 클라이언트 생성 일자입니다. JSON 출력 시 'create_at'으로 표시됩니다.
     */
    @JsonProperty("createdAt") // 이 필드만 snake_case로 지정
    private LocalDate createAt;
}
