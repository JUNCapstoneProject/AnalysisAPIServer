package com.AnalysisAPIserver.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * API 키 엔티티이다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_keys")
public final class ApiKey {

    /**
     * 키 ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 클라이언트 ID.
     */
    @Column(nullable = false, unique = true)
    private String clientId;

    /**
     * 클라이언트 시크릿.
     */
    @Column(nullable = false)
    private String clientSecret;

    /**
     * 연결된 사용자.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
