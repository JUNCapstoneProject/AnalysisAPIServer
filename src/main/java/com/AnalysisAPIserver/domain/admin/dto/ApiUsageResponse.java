package com.AnalysisAPIserver.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 사용량 응답 DTO이다.
 */
@Getter
@AllArgsConstructor
public final class ApiUsageResponse {

    /**
     * 전체 요청 수.
     */
    private final long totalRequests;

    /**
     * 고유 사용자 수.
     */
    private final long uniqueUsers;

    /**
     * 사용자당 평균 요청 수.
     */
    private final long avgRequestsPerUser;
}
