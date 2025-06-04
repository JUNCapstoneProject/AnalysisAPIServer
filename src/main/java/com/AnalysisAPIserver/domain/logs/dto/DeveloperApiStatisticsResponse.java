package com.AnalysisAPIserver.domain.logs.dto;



import lombok.Builder;
import lombok.Data; // 또는 @Getter 등 필요한 어노테이션만 사용

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 개발자의 API 사용 통계 응답을 위한 DTO 클래스입니다.
 */
@Data // 간결함을 위해 @Data 사용, 필요시 @Getter, @Setter 등으로 분리
@Builder
public class DeveloperApiStatisticsResponse {

    /**
     * 개발자 ID.
     */
    private Long developerId;

    /**
     * 사용자 이름 (이메일).
     */
    private String userName; // 이메일이 저장될 필드

    /**
     * 총 API 요청 횟수.
     */
    private int totalRequestCount;

    /**
     * API 호출 성공 횟수.
     */
    private int successCount;

    /**
     * API 호출 실패 횟수.
     */
    private int errorCount;

    /**
     * 애플리케이션별 API 요청 횟수 (애플리케이션 이름: 횟수).
     */
    private Map<String, Integer> requestCountByApplication; // 애플리케이션 이름: 횟수

    /**
     * API 카테고리별 요청 횟수 (API 카테고리 이름: 횟수).
     */
    private Map<String, Integer> requestCountByApiCategory; // API 카테고리 이름: 횟수

    /**
     * 마지막 API 요청 시간.
     */
    private LocalDateTime lastRequestTime;
}
