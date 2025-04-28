package com.AnalysisAPIserver.domain.client.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트 엔티티이다.
 */
@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public final class Client {

    /**
     * 클라이언트 내부 ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 개발자 ID.
     */
    private Long developerId;

    /**
     * 클라이언트 ID.
     */
    @Column(unique = true, nullable = false)
    private String clientId;

    /**
     * 클라이언트 이름.
     */
    private String name;
}
