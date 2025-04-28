package com.AnalysisAPIserver.domain.logs.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * API 로그 DTO이다.
 */
@Data
@AllArgsConstructor
public final class ApiLogDto {

    /**
     * 호출한 엔드포인트.
     */
    private String endpoint;

    /**
     * 호출한 HTTP 메소드.
     */
    private String method;

    /**
     * 호출 시각.
     */
    private LocalDateTime timestamp;
}
