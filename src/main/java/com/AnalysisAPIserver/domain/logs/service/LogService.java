package com.AnalysisAPIserver.domain.logs.service;

import com.AnalysisAPIserver.domain.logs.dto.ApiLogRequest;
import com.AnalysisAPIserver.domain.logs.dto.ApiLogResponse;
import com.AnalysisAPIserver.domain.logs.dto.ApiLogDto;
import com.AnalysisAPIserver.domain.logs.entity.ApiLog;
import com.AnalysisAPIserver.domain.logs.exception.LogException;
import com.AnalysisAPIserver.domain.logs.repository.LogRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * API 로그 서비스이다.
 */
@Service
@RequiredArgsConstructor
public class LogService {

    /**
     * 로그 리포지토리.
     */
    private final LogRepository logRepository;

    /**
     * API 로그를 저장한다.
     *
     * @param request API 로그 요청
     */
    @Transactional
    public void saveApiLog(final ApiLogRequest request) {
        if (request.getClientId() == null || request.getClientId().isEmpty()) {
            throw new LogException("클라이언트 ID는 필수입니다.");
        }

        ApiLog log = ApiLog.builder()
                .clientId(request.getClientId())
                .endpoint(request.getEndpoint())
                .method(request.getMethod())
                .timestamp(request.getTimestamp())
                .build();

        this.logRepository.save(log);
    }

    /**
     * 클라이언트 ID로 API 로그를 조회한다.
     *
     * @param clientId 클라이언트 ID
     * @return API 로그 응답
     */
    @Transactional(readOnly = true)
    public ApiLogResponse getDeveloperLogs(final String clientId) {
        List<ApiLog> logs = this.logRepository.findByClientId(clientId);

        List<ApiLogDto> dtoList = logs.stream()
                .map(log -> new ApiLogDto(
                        log.getEndpoint(),
                        log.getMethod(),
                        log.getTimestamp()))
                .collect(Collectors.toList());

        return new ApiLogResponse(clientId, dtoList);
    }
}
