package com.AnalysisAPIserver.domain.DB_Table.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ApiStatics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiStatics {

    /**
     * 요청 ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    /**
     * 요청한 개발자.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", nullable = false)
    private ApiUser developer;

    /**
     * 요청한 애플리케이션.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private Application application;

    /**
     * 요청한 API 카테고리.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_category_id", nullable = false)
    private ApiCategory apiCategory;

    /**
     * 응답 상태 코드.
     */
    @Column(name = "status_code", nullable = false)
    private int statusCode;

    /**
     * 요청 시간.
     */
    @Column(name = "request_at", nullable = false)
    private LocalDateTime requestAt;

    /**
     * 관련된 에러 로그 (있는 경우).
     */
    @OneToOne(mappedBy = "apiStatics", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY) // Wrapped long line.
    private ErrorLogs errorLogs;

    /**
     * 엔티티 저장 전에 requestAt 필드를 현재 시간으로 설정합니다.
     * 서브클래스에서 재정의할 수 있지만, 기본 동작은 유지해야 합니다.
     */
    @PrePersist
    protected void onCreate() {
        this.requestAt = LocalDateTime.now();
    }
}
