package com.AnalysisAPIserver.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 분석 상태 조회 응답 DTO이다.
 */
@Data
@AllArgsConstructor
public final class AnalysisStatusResponse {

    /**
     * 요청 ID.
     */
    private String requestId;

    /**
     * 상태.
     */
    private String status;
}
