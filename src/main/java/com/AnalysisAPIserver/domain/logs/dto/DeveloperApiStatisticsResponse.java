package com.AnalysisAPIserver.domain.logs.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class DeveloperApiStatisticsResponse {

    /**
     * 개발자 ID.
     */
    private Long developerId;
    /**
     * 사용자 이름.
     */
    private String userName;
    /**
     * 총 요청 수.
     */
    private int totalRequestCount;
    /**
     * 성공적인 요청 수.
     */
    private int successCount;
    /**
     * 오류 요청 수.
     */
    private int errorCount;

    /**
     * 애플리케이션별 요청 수.
     */
    private Map<String, Integer> requestCountByApplication;
    /**
     * API 카테고리별 요청 수.
     */
    private Map<String, Integer> requestCountByApiCategory;

    /**
     * 마지막 요청 시간.
     */
    private LocalDateTime lastRequestTime;
}
