package com.AnalysisAPIserver.domain.analysis.repository;



import com.AnalysisAPIserver.domain.analysis.entity.AnalysisLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<AnalysisLog, Long> {
    List<AnalysisLog> findByClientId(String clientId);
}