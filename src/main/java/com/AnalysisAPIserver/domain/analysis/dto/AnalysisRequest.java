package com.AnalysisAPIserver.domain.analysis.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {
    private String query;
    private String clientId;
}
