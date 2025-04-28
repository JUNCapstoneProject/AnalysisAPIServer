package com.AnalysisAPIserver.domain.admin.repository;

import com.AnalysisAPIserver.domain.admin.entity.ApiUsageLog;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * API 사용 기록 리포지토리이다.
 */
@Repository
public interface
ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    /**
     * 특정 기간 내 요청 수 조회.
     *
     * @param start 시작 시간
     * @param end 끝 시간
     * @return 요청 수
     */
    @Query("SELECT COUNT(a) FROM ApiUsageLog a "
            + "WHERE a.requestTime BETWEEN :start AND :end")
    long countRequestsBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 특정 기간 내 고유 사용자 수 조회.
     *
     * @param start 시작 시간
     * @param end 끝 시간
     * @return 사용자 수
     */
    @Query("SELECT COUNT(DISTINCT a.clientId) FROM ApiUsageLog a "
            + "WHERE a.requestTime BETWEEN :start AND :end")
    long countUniqueUsersBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 전체 요청 수 조회.
     *
     * @return 요청 수
     */
    @Query("SELECT COUNT(a) FROM ApiUsageLog a")
    long countTotalRequests();

    /**
     * 전체 고유 사용자 수 조회.
     *
     * @return 사용자 수
     */
    @Query("SELECT COUNT(DISTINCT a.clientId) FROM ApiUsageLog a")
    long countUniqueUsers();
}
