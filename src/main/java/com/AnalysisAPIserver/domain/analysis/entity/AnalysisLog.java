package com.AnalysisAPIserver.domain.analysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 분석 로그 엔티티이다.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "analysis_logs")
public class AnalysisLog {

    /**
     * PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 분석 쿼리.
     */
    @Column(nullable = false)
    private String query;

    /**
     * 분석 결과.
     */
    @Column
    private String result;

    /**
     * 클라이언트 ID.
     */
    @Column(nullable = false)
    private String clientId;

    /**
     * 생성 시간.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 생성자.
     *
     * @param queryParam 쿼리
     * @param resultParam 결과
     * @param clientIdParam 클라이언트 ID
     */
    public AnalysisLog(final String queryParam, final String resultParam,
                       final String clientIdParam) {
        this.query = queryParam;
        this.result = resultParam;
        this.clientId = clientIdParam;
    }
}
