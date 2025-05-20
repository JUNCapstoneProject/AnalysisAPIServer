package com.AnalysisAPIserver.domain.DB_Table.repository;


import com.AnalysisAPIserver.domain.DB_Table.entity.ErrorLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErrorLogsRepository extends JpaRepository<ErrorLogs, Long> {

    /**
     * 요청 ID로 에러 로그를 조회합니다.
     * @param requestId 요청 ID.
     * @return 해당 요청 ID를 가진 에러 로그의 Optional.
     */
    Optional<ErrorLogs> findByRequestId(Long requestId);
}
