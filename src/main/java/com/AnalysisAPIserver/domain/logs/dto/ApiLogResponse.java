package com.AnalysisAPIserver.domain.logs.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiLogResponse {
    private String clientId;
    private List<ApiLogDto> logs;
}
