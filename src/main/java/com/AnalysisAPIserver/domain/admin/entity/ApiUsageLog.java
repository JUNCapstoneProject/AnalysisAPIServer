package com.AnalysisAPIserver.domain.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 사용 기록 엔티티이다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_usage_logs")
public class ApiUsageLog {

    /**
     * PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 요청한 클라이언트 ID.
     */
    @Column(nullable = false)
    private String clientId;

    /**
     * 요청한 엔드포인트.
     */
    @Column(nullable = false)
    private String endpoint;

    /**
     * 요청 시간.
     */
    @Column(nullable = false)
    private LocalDateTime requestTime;
}
