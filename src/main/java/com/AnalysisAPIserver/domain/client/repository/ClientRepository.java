package com.AnalysisAPIserver.domain.client.repository;

import com.AnalysisAPIserver.domain.client.entity.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 클라이언트 리포지토리이다.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * 개발자 ID로 클라이언트 조회.
     *
     * @param developerId 개발자 ID
     * @return 클라이언트 리스트
     */
    List<Client> findByDeveloperId(Long developerId);

    /**
     * 클라이언트 ID로 클라이언트 조회.
     *
     * @param clientId 클라이언트 ID
     * @return 클라이언트 Optional
     */
    Optional<Client> findByClientId(String clientId);
}
