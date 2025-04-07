package com.AnalysisAPIserver.domain.auth.repository;

import com.AnalysisAPIserver.domain.auth.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByClientId(String clientId);
    Optional<ApiKey> findByUserId(Long userId);
}