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

/**
 * 분석 비즈니스 로직을 처리하는 서비스이다.
 */
@Service
@RequiredArgsConstructor
public class AnalysisService {

    /**
     * 분석 리포지토리.
     */
    private final AnalysisRepository analysisRepository;

    /**
     * 라우터 서비스.
     */
    private final RouterService routerService;

    /**
     * 분석 요청을 라우팅한다.
     *
     * @param request 분석 요청
     * @return 분석 결과
     */
    public AnalysisResponse routeRequest(final AnalysisRequest request) {
        if (request.getQuery() == null || request.getQuery().isEmpty()) {
            throw new AnalysisException("분석할 쿼리를 입력하세요.");
        }
        return this.routerService.routeToStockAnalysisAPI(request);
    }

    /**
     * 분석 상태를 조회한다.
     *
     * @param requestId 요청 ID
     * @return 분석 상태
     */
    public AnalysisStatusResponse getAnalysisStatus(final String requestId) {
        return new AnalysisStatusResponse(requestId, "아직 구현되지 않음");
    }

    /**
     * 분석 결과를 저장한다.
     *
     * @param request 분석 결과 저장 요청
     */
    public void saveAnalysisResult(final AnalysisResultRequest request) {
        AnalysisLog log = this.analysisRepository.findById(
                        Long.parseLong(request.getRequestId()))
                .orElseThrow(() -> new AnalysisException("분석 요청을 찾을 수 없습니다."));
        log.setResult(request.getResult());
        this.analysisRepository.save(log);
    }
}
