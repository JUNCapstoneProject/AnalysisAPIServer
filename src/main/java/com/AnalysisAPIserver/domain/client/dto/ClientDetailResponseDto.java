package com.AnalysisAPIserver.domain.client.dto;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 클라이언트 상세 정보 조회 시 응답으로 사용되는 DTO입니다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDetailResponseDto {
    /**
     * 클라이언트의 고유 ID입니다.
     */
    private String clientId;
    /**
     * 클라이언트의 비밀 키입니다.
     */
    private String clientSecret;
    /**
     * 클라이언트(애플리케이션)의 이름입니다.
     */
    private String appName;
    /**
     * 클라이언트의 현재 상태 (예: "ACTIVE", "INACTIVE")입니다.
     */
    private String status;
    /**
     * 클라이언트가 사용하는 API의 카테고리 ID입니다.
     */
    private String apiCategoryId;
    /**
     * 클라이언트(애플리케이션)의 카테고리 ID입니다.
     */
    private String appCategoryId;
    /**
     * 클라이언트 생성 일자입니다.
     */
    private LocalDate createdAt;
}
