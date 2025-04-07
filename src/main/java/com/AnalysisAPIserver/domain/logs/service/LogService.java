package com.AnalysisAPIserver.domain.logs.service;

import com.AnalysisAPIserver.domain.logs.dto.ApiLogRequest;
import com.AnalysisAPIserver.domain.logs.dto.ApiLogResponse;
import com.AnalysisAPIserver.domain.logs.dto.ApiLogDto;
import com.AnalysisAPIserver.domain.logs.entity.ApiLog;
import com.AnalysisAPIserver.domain.logs.exception.LogException;
import com.AnalysisAPIserver.domain.logs.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional
    public void saveApiLog(ApiLogRequest request) {
        if (request.getClientId() == null || request.getClientId().isEmpty()) {
            throw new LogException("클라이언트 ID는 필수입니다.");
        }

        ApiLog log = ApiLog.builder()
                .clientId(request.getClientId())
                .endpoint(request.getEndpoint())
                .method(request.getMethod())
                .timestamp(request.getTimestamp())
                .build();

        logRepository.save(log);
    }

    public ApiLogResponse getDeveloperLogs(String clientId) {
        List<ApiLog> logs = logRepository.findByClientId(clientId);

        List<ApiLogDto> dtoList = logs.stream()
                .map(log -> new ApiLogDto(log.getEndpoint(), log.getMethod(), log.getTimestamp()))
                .collect(Collectors.toList());

        return new ApiLogResponse(clientId, dtoList);
    }
}