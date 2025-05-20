package com.AnalysisAPIserver.domain.DB_Table.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "Application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    /**
     * 애플리케이션 ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long id;

    /**
     * 애플리케이션 소유자.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private ApiUser owner;

    /**
     * 애플리케이션 카테고리.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_category_id")
    private AppCategory appCategory;

    /**
     * 관련된 API 카테고리.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_category_id")
    private ApiCategory apiCategory;

    /**
     * 클라이언트 ID (고유).
     */
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    /**
     * 클라이언트 Secret.
     */
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    /**
     * 애플리케이션 이름.
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * 콜백 URL.
     */
    @Column(name = "callback_url")
    private String callbackUrl;

    /**
     * 생성 시간.
     */
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createdAt;

}
