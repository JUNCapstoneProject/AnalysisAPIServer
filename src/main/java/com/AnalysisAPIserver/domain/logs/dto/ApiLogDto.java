package com.AnalysisAPIserver.domain.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiLogDto {
    private String endpoint;
    private String method;
    private LocalDateTime timestamp;
}