package com.AnalysisAPIserver.domain.auth.repository;

import com.AnalysisAPIserver.domain.auth.entity.ApiKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API 키 리포지토리이다.
 */
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    /**
     * clientId로 API 키를 조회한다.
     *
     * @param clientId 클라이언트 ID
     * @return API 키
     */
    Optional<ApiKey> findByClientId(String clientId);

    /**
     * userId로 API 키를 조회한다.
     *
     * @param userId 사용자 ID
     * @return API 키
     */
    Optional<ApiKey> findByUserId(Long userId);
}
