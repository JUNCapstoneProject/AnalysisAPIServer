package com.AnalysisAPIserver.domain.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsSummaryDto {
    /**
     * 총 요청 수.
     */
    private long totalRequests;
    /**
     * 총 오류 수.
     */
    private long totalErrors;
    /**
     * 총 사용자 수.
     */
    private long totalUsers;
    /**
     * 총 애플리케이션 수.
     */
    private long totalApps;
    /**
     * 총 카테고리 수.
     */
    private long totalCategories;
}
