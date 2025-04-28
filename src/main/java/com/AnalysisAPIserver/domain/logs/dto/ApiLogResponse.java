package com.AnalysisAPIserver.domain.logs.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * API 로그 응답 DTO이다.
 */
@Data
@AllArgsConstructor
public final class ApiLogResponse {

    /**
     * 클라이언트 ID.
     */
    private String clientId;

    /**
     * 로그 리스트.
     */
    private List<ApiLogDto> logs;
}
