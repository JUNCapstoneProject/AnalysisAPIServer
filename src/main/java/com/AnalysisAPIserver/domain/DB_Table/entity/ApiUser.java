package com.AnalysisAPIserver.domain.DB_Table.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 엔티티이다.
 */
@Entity
@Table(name = "ApiUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiUser {

    /**
     * 개발자 ID.
     */
    @Id
    @Column(name = "developer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long developerId;

    /**
     * 사용자 이름.
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 사용자 타입 (예: 개발자, 관리자 등).
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 생성 시간.
     */
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    /**
     * 이 사용자가 소유한 애플리케이션 목록.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL,
            orphanRemoval = true) // Wrapped long line.
    private List<Application> applications;

    /**
     * 엔티티 생성 시 createdAt 필드를 현재 시간으로 설정합니다.
     * 서브클래스에서 재정의할 수 있지만, 기본 동작은 유지해야 합니다.
     */
    @CreationTimestamp
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
