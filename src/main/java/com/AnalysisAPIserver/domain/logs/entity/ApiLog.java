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
 * API 로그 엔티티이다.
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "api_logs")
public final class ApiLog {

    /**
     * 로그 ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 클라이언트 ID.
     */
    @Column(nullable = false)
    private String clientId;

    /**
     * 요청 엔드포인트.
     */
    @Column(nullable = false)
    private String endpoint;

    /**
     * HTTP 메소드(GET, POST 등).
     */
    @Column(nullable = false)
    private String method;

    /**
     * 요청 발생 시각.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * ApiLog 생성자.
     *
     * @param clientId 클라이언트 ID
     * @param endpoint 요청 엔드포인트
     * @param method HTTP 메소드
     * @param timestamp 요청 시각
     */
    @Builder
    public ApiLog(
            final String clientId,
            final String endpoint,
            final String method,
            final LocalDateTime timestamp) {
        this.clientId = clientId;
        this.endpoint = endpoint;
        this.method = method;
        this.timestamp = timestamp;
    }
}
