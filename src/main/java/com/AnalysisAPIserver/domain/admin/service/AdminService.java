package com.AnalysisAPIserver.domain.admin.service;

import com.AnalysisAPIserver.domain.admin.dto.ApiUsageResponse;
import com.AnalysisAPIserver.domain.admin.exception.AdminErrorCode;
import com.AnalysisAPIserver.domain.admin.exception.AdminException;
import com.AnalysisAPIserver.domain.admin.repository.ApiUsageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 관리자 서비스 클래스이다.
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    /**
     * API 사용 기록 리포지토리.
     */
    private final ApiUsageLogRepository apiUsageLogRepository;

    /**
     * 전체 API 사용량 통계 조회.
     *
     * @return API 사용량 응답
     */
    public ApiUsageResponse getApiUsageStats() {
        long totalRequests = this.apiUsageLogRepository.countTotalRequests();
        long uniqueUsers = this.apiUsageLogRepository.countUniqueUsers();

        if (totalRequests == 0) {
            throw new AdminException(AdminErrorCode.API_USAGE_DATA_NOT_FOUND);
        }

        long avgRequestsPerUser = (uniqueUsers == 0)
                ? 0
                : totalRequests / uniqueUsers;
        return new ApiUsageResponse(
                totalRequests,
                uniqueUsers,
                avgRequestsPerUser
        );
    }
}
