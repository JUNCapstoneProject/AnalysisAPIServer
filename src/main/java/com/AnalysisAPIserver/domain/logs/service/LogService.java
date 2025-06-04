package com.AnalysisAPIserver.domain.logs.service;


import com.AnalysisAPIserver.domain.DB_Table.entity.ApiStatics;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiCategory;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiStaticsRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
import com.AnalysisAPIserver.domain.auth.exception.ResourceNotFoundException;
import com.AnalysisAPIserver.domain.logs.dto.DeveloperApiStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogService {

    /**
     * 이 서비스 클래스에서 사용할 로거(Logger) 객체입니다.
     */
    private static final Logger LOG = LoggerFactory.getLogger(LogService.class);
    /**
     * API 사용자 정보에 접근하기 위한 리포지토리입니다.
     */
    private final ApiUserRepository apiUserRepository;
    /**
     * API 통계 정보에 접근하기 위한 리포지토리입니다.
     */
    private final ApiStaticsRepository apiStaticsRepository;

    /**
     * 성공으로 간주되는 HTTP 상태 코드의 임계값입니다.
     * 이 값 미만의 상태 코드는 성공으로 처리됩니다.
     */
    private static final int STATUS_CODE_SUCCESS_THRESHOLD = 400;

    /**
     * 특정 개발자의 API 통계 정보를 조회합니다.
     *
     * @param developerId 통계를 조회할 개발자 ID.
     * @return 개발자 API 통계 응답 객체.
     * @throws ResourceNotFoundException 해당 developerId의 사용자를 찾을 수 없는 경우.
     */
    @Transactional(readOnly = true)
    public DeveloperApiStatisticsResponse getDeveloperStatistics(
            final Long developerId) {
        LOG.debug("Fetching statistics for developerId: {}", developerId);
        ApiUser developer = apiUserRepository.findById(developerId)
                .orElseThrow(() -> {
                    LOG.warn("Developer not found with ID: {}", developerId);
                    String errorMessage = "존재하지 않는 개발자 ID입니다: " + developerId;
                    return new ResourceNotFoundException(errorMessage);
                });

        List<ApiStatics> staticsList =
                apiStaticsRepository.findByDeveloper(developer);
        LOG.debug("Found {} statics records for developerId: {}",
                staticsList.size(), developerId);

        int totalRequest = staticsList.size();
        int successCount = 0;
        Map<String, Integer> requestByApp = new HashMap<>();
        Map<String, Integer> requestByCategory = new HashMap<>();
        LocalDateTime lastRequestTime = null;

        for (ApiStatics statics : staticsList) {
            if (statics.getStatusCode() < STATUS_CODE_SUCCESS_THRESHOLD) {
                successCount++;
            }

            Application app = statics.getApplication();
            if (app != null && app.getAppName() != null) {
                requestByApp.put(app.getAppName(),
                        requestByApp.getOrDefault(app.getAppName(), 0) + 1);
            } else {
                requestByApp.put("알 수 없는 앱",
                        requestByApp.getOrDefault("알 수 없는 앱", 0) + 1);
                LOG.warn("ApiStatics record (ID: {}) has null "
                        + "application or appName.", statics.getRequestId());
            }

            ApiCategory category = statics.getApiCategory();
            if (category != null && category.getApiCategoryName() != null) {
                requestByCategory.put(category.getApiCategoryName(),
                        requestByCategory.getOrDefault(
                                category.getApiCategoryName(), 0) + 1);
            } else {
                requestByCategory.put("알 수 없는 카테고리",
                        requestByCategory.getOrDefault(
                                "알 수 없는 카테고리", 0) + 1);
                LOG.warn("ApiStatics record (ID: {}) has null "
                        + "apiCategory or apiCategoryName.",
                        statics.getRequestId());
            }

            if (statics.getRequestAt() != null) {
                if (lastRequestTime == null
                        || statics.getRequestAt().isAfter(lastRequestTime)) {
                    lastRequestTime = statics.getRequestAt();
                }
            }
        }
        int errorCount = totalRequest - successCount;

        return DeveloperApiStatisticsResponse.builder()
                .developerId(developer.getDeveloperId())
                .userName(developer.getUserName())
                .totalRequestCount(totalRequest)
                .successCount(successCount)
                .errorCount(errorCount)
                .requestCountByApplication(requestByApp)
                .requestCountByApiCategory(requestByCategory)
                .lastRequestTime(lastRequestTime)
                .build();
    }
}
