package com.AnalysisAPIserver.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 분석 요청 DTO이다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class AnalysisRequest {

    /**
     * 분석 쿼리.
     */
    private String query;

    /**
     * 클라이언트 ID.
     */
    private String clientId;
}
