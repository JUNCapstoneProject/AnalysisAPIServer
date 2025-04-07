package com.AnalysisAPIserver.domain.analysis.dto;



import lombok.Data;

@Data
public class AnalysisResultRequest {
    private String requestId;
    private String result;
}
