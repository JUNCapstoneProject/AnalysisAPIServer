package com.AnalysisAPIserver.domain.analysis.router;

import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponse;
import com.AnalysisAPIserver.domain.analysis.entity.AnalysisLog;
import com.AnalysisAPIserver.domain.analysis.exception.AnalysisException;
import com.AnalysisAPIserver.domain.analysis.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 분석 요청을 외부 API로 라우팅하는 서비스이다.
 */
@Service
@RequiredArgsConstructor
public final class RouterService {

    /**
     * RestTemplate 인스턴스이다.
     */
    private final RestTemplate restTemplate;

    /**
     * 분석 저장소이다.
     */
    private final AnalysisRepository analysisRepository;

    /**
     * 주식 분석 API URL이다.
     */
    private static final String STOCK_ANALYSIS_API_URL =
            "http://{host}/api/analysis/stock/pipeline";

    /**
     * 주식 분석 API로 요청을 라우팅한다.
     *
     * @param request 분석 요청
     * @return 분석 응답
     */
    public
    AnalysisResponse routeToStockAnalysisAPI(final AnalysisRequest request) {
        try {
            String result = this.restTemplate.postForObject(
                    STOCK_ANALYSIS_API_URL,
                    request,
                    String.class
            );

            AnalysisLog log = new AnalysisLog(
                    request.getQuery(),
                    result,
                    request.getClientId()
            );
            this.analysisRepository.save(log);

            return new AnalysisResponse(result);
        } catch (Exception e) {
            throw new AnalysisException(
                    "StockAnalysisAPI 라우팅 실패: " + e.getMessage());
        }
    }
}
