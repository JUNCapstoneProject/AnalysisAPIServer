package com.AnalysisAPIserver.domain.logs.repository;

import com.AnalysisAPIserver.domain.logs.entity.ApiLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API 로그 리포지토리이다.
 */
@Repository
public interface LogRepository extends JpaRepository<ApiLog, Long> {

    /**
     * 클라이언트 ID로 로그 조회.
     *
     * @param clientId 클라이언트 ID
     * @return API 로그 리스트
     */
    List<ApiLog> findByClientId(String clientId);
}
