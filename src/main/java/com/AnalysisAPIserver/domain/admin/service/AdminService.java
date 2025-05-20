package com.AnalysisAPIserver.domain.admin.service;

import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 관리자 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    /**
     * API 사용자 리포지토리.
     */
    private final ApiUserRepository apiUsageLogRepository;

    /*
     * 전체 API 사용량 통계를 조회합니다.
     * 예외 상황에서는 AdminException이 발생할 수 있습니다.
     *
     * @return API 사용량 응답
     *
     * public ApiUsageResponse getApiUsageStats() {
     *     long totalRequests = this.apiUsageLogRepository.countTotalRequests();
     *     long uniqueUsers = this.apiUsageLogRepository.countUniqueUsers();
     *
     *     if (totalRequests == 0) {
     *         throw
     *          new AdminException(AdminErrorCode.API_USAGE_DATA_NOT_FOUND);
     *     }
     *
     *     long avgRequestsPerUser = (uniqueUsers == 0)
     *         ? 0
     *         : totalRequests / uniqueUsers;
     *
     *     return new ApiUsageResponse(
     *         totalRequests,
     *         uniqueUsers,
     *         avgRequestsPerUser
     *     );
     * }
     */
}
