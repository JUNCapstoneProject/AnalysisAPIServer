package com.AnalysisAPIserver.domain.client.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


/**
 * 클라이언트 목록 조회 시 각 클라이언트 정보를 담는 DTO입니다.
 * JSON 직렬화 시 속성 이름은 snake_case로 변환됩니다.
 */
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientListResponseDto {

    /**
     * 클라이언트(애플리케이션)의 이름입니다.
     */
    private String appName;
    /**
     * 클라이언트의 고유 ID입니다.
     */
    private String clientId;
    /**
     * 클라이언트가 사용하는 API의 카테고리 ID입니다.
     */
    private String apiCategoryId;
    /**
     * 클라이언트(애플리케이션)의 카테고리 ID입니다.
     */
    private String appCategoryId;
    /**
     * 클라이언트의 현재 상태입니다.
     */
    private String status;
    /**
     * 클라이언트 생성 일자입니다. JSON 출력 시 'create_at'으로 표시됩니다.
     */
    private LocalDate createAt;
}
