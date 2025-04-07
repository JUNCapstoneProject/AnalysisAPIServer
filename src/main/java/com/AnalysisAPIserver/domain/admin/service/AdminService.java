package com.AnalysisAPIserver.domain.admin.service;



import com.AnalysisAPIserver.domain.admin.dto.ApiUsageResponse;
import com.AnalysisAPIserver.domain.admin.exception.AdminErrorCode;
import com.AnalysisAPIserver.domain.admin.exception.AdminException;
import com.AnalysisAPIserver.domain.admin.repository.ApiUsageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ApiUsageLogRepository apiUsageLogRepository;

    public ApiUsageResponse getApiUsageStats() {
        long totalRequests = apiUsageLogRepository.countTotalRequests();
        long uniqueUsers = apiUsageLogRepository.countUniqueUsers();

        if (totalRequests == 0) {
            throw new AdminException(AdminErrorCode.API_USAGE_DATA_NOT_FOUND);
        }

        long avgRequestsPerUser = (uniqueUsers == 0) ? 0 : totalRequests / uniqueUsers;

        return new ApiUsageResponse(totalRequests, uniqueUsers, avgRequestsPerUser);
    }
}
