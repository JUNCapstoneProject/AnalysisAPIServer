package com.AnalysisAPIserver.domain.logs.service;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiStatics;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;

import com.AnalysisAPIserver.domain.DB_Table.repository.ApiStaticsRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;



import com.AnalysisAPIserver.domain.logs.dto.DeveloperApiStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class LogService {

    /**
     * 사용자 정보에 접근하기 위한 리포지토리.
     */
    private final ApiUserRepository apiUserRepository;
    /**
     * API 통계 정보에 접근하기 위한 리포지토리.
     */
    private final ApiStaticsRepository apiStaticsRepository;

    /**
     * 상태 코드.
     */
    private static final int
            STATUS_CODE_SUCCESS_THRESHOLD = 400;

    /**
     * 특정 개발자의 API 통계 정보를 조회합니다.
     * 이 메소드는 확장될 수 있지만, 현재 로직은 유지되어야 합니다.
     *
     * @param developerId 통계를 조회할 개발자 ID.
     * @return 개발자 API 통계 응답 객체.
     */
    public DeveloperApiStatisticsResponse getDeveloperStatistics(
            final Long developerId) { // LineLength, FinalParameters 적용
        ApiUser developer = apiUserRepository.findById(developerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 개발자 ID입니다.")); // LineLength 해결

        List<ApiStatics> staticsList = apiStaticsRepository.findByDeveloper(
                developer); // LineLength 해결

        int totalRequest = staticsList.size();
        int successCount = (int) staticsList
                .stream()
                .filter(s ->
                        s.getStatusCode()
                                < STATUS_CODE_SUCCESS_THRESHOLD)
                .count();
        int errorCount = totalRequest - successCount;

        Map<String, Integer> requestByApp = new HashMap<>();
        Map<String, Integer> requestByCategory = new HashMap<>();
        LocalDateTime lastRequestTime = null;

        for (ApiStatics statics : staticsList) {
            String appName = statics.getApplication()
                    .getAppName();
            requestByApp.put(appName, requestByApp
                    .getOrDefault(appName, 0) + 1);

            // API 카테고리별 카운트
            String apiCategoryName = statics
                    .getApiCategory()
                    .getApiCategoryName();
            requestByCategory.put(apiCategoryName,
                    requestByCategory
                            .getOrDefault(apiCategoryName, 0) + 1);

            // 마지막 요청 시간
            if (lastRequestTime == null || statics
                    .getRequestAt().isAfter(lastRequestTime)) {
                lastRequestTime = statics.getRequestAt();
            }
        }

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
