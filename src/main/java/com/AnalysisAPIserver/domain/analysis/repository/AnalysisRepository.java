package com.AnalysisAPIserver.domain.analysis.repository;

import com.AnalysisAPIserver.domain.analysis.entity.AnalysisLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 분석 로그 리포지토리이다.
 */
@Repository
public interface AnalysisRepository extends JpaRepository<AnalysisLog, Long> {

    /**
     * 클라이언트 ID로 분석 로그 조회.
     *
     * @param clientId 클라이언트 ID
     * @return 분석 로그 리스트
     */
    List<AnalysisLog> findByClientId(String clientId);
}
