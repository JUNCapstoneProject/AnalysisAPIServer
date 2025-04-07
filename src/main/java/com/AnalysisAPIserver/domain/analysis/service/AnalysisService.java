package com.AnalysisAPIserver.domain.analysis.service;



import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponse;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResultRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisStatusResponse;
import com.AnalysisAPIserver.domain.analysis.entity.AnalysisLog;
import com.AnalysisAPIserver.domain.analysis.exception.AnalysisException;
import com.AnalysisAPIserver.domain.analysis.repository.AnalysisRepository;
import com.AnalysisAPIserver.domain.analysis.router.RouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final RouterService routerService;

    public AnalysisResponse routeRequest(AnalysisRequest request) {
        if (request.getQuery() == null || request.getQuery().isEmpty()) {
            throw new AnalysisException("분석할 쿼리를 입력하세요.");
        }

        // 컨테이너 라우팅 로직 위임
        return routerService.routeToStockAnalysisAPI(request);
    }

    public AnalysisStatusResponse getAnalysisStatus(String requestId) {
        return new AnalysisStatusResponse(requestId, "아직 구현되지 않음");
    }

    public void saveAnalysisResult(AnalysisResultRequest request) {
        AnalysisLog log = analysisRepository.findById(Long.parseLong(request.getRequestId()))
                .orElseThrow(() -> new AnalysisException("분석 요청을 찾을 수 없습니다."));
        log.setResult(request.getResult());
        analysisRepository.save(log);
    }
}
