package com.AnalysisAPIserver.domain.admin.repository;

import com.AnalysisAPIserver.domain.admin.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    @Query("SELECT COUNT(a) FROM ApiUsageLog a WHERE a.requestTime BETWEEN :start AND :end")
    long countRequestsBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT a.clientId) FROM ApiUsageLog a WHERE a.requestTime BETWEEN :start AND :end")
    long countUniqueUsersBetween(LocalDateTime start, LocalDateTime end);

    // ✅ 전체 기간 대상 집계
    @Query("SELECT COUNT(a) FROM ApiUsageLog a")
    long countTotalRequests();

    @Query("SELECT COUNT(DISTINCT a.clientId) FROM ApiUsageLog a")
    long countUniqueUsers();
}
