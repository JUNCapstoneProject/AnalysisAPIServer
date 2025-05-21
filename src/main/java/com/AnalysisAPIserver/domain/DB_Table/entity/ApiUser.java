package com.AnalysisAPIserver.domain.DB_Table.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * API 사용자 정보를 나타내는 엔티티 클래스입니다.
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
     * 개발자의 고유 식별자입니다. (Primary Key)
     */
    @Id
    @Column(name = "developer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long developerId;

    /**
     * 사용자의 이름 또는 식별자입니다. (OAuth에서 주로 이메일)
     * Null 값을 허용하지 않습니다.
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 사용자의 유형입니다. (예: "개발자")
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 계정 생성 시간입니다.
     * Null 값을 허용하지 않으며, 업데이트되지 않습니다.
     */
    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 해당 사용자가 소유한 애플리케이션 목록입니다.
     * ApiUser가 삭제되면 연관된 Application도 함께 삭제됩니다.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Application> applications;
}
