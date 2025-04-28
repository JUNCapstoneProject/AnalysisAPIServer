package com.AnalysisAPIserver.domain.analysis.dto;

import lombok.Data;

/**
 * 분석 결과 저장 요청 DTO이다.
 */
@Data
public final class AnalysisResultRequest {

    /**
     * 요청 ID.
     */
    private String requestId;

    /**
     * 결과 데이터.
     */
    private String result;
}
