package com.AnalysisAPIserver.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 분석 결과 응답 DTO이다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class AnalysisResponse {

    /**
     * 분석 결과.
     */
    private String result;
}
