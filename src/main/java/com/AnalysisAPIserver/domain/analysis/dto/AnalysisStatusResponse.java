package com.AnalysisAPIserver.domain.analysis.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisStatusResponse {
    private String requestId;
    private String status;
}