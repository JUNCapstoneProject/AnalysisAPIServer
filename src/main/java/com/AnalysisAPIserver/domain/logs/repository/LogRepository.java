package com.AnalysisAPIserver.domain.logs.repository;



import com.AnalysisAPIserver.domain.logs.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<ApiLog, Long> {
    List<ApiLog> findByClientId(String clientId);
}