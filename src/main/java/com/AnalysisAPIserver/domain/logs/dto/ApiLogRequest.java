package com.AnalysisAPIserver.domain.logs.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiLogRequest {
    private String clientId;
    private String endpoint;
    private String method;
    private LocalDateTime timestamp;
}