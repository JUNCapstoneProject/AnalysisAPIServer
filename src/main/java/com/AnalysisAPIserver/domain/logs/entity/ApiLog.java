package com.AnalysisAPIserver.domain.logs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API 로그 엔티티 클래스이다.
 * <p>
 * 클라이언트 요청에 대한 엔드포인트, 메소드, 요청 시각 등의 정보를 저장한다.
 * </p>
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "api_logs")
public final class ApiLog {

    /**
     * 로그 ID (자동 생성).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 요청을 보낸 클라이언트 ID.
     */
    @Column(nullable = false)
    private String clientId;

    /**
     * 요청한 엔드포인트 URI.
     */
    @Column(nullable = false)
    private String endpoint;

    /**
     * 요청 메소드 (예: GET, POST 등).
     */
    @Column(nullable = false)
    private String method;

    /**
     * 요청 발생 시각 (LocalDateTime).
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * ApiLog 생성자.
     *
     * @param clientIdParam 클라이언트 ID
     * @param endpointParam 요청 엔드포인트
     * @param methodParam HTTP 요청 메소드
     * @param timestampParam 요청 발생 시각
     */
    @Builder
    public ApiLog(
            final String clientIdParam,
            final String endpointParam,
            final String methodParam,
            final LocalDateTime timestampParam) {
        this.clientId = clientIdParam;
        this.endpoint = endpointParam;
        this.method = methodParam;
        this.timestamp = timestampParam;
    }
}
