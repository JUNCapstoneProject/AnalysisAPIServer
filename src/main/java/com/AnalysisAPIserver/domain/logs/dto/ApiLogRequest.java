package com.AnalysisAPIserver.domain.logs.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 로그 요청 DTO이다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ApiLogRequest {

    /**
     * 클라이언트 ID.
     */
    private String clientId;

    /**
     * 요청한 엔드포인트.
     */
    private String endpoint;

    /**
     * 요청 메소드.
     */
    private String method;

    /**
     * 요청 시각.
     */
    private LocalDateTime timestamp;
}
