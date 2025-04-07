package com.AnalysisAPIserver.domain.analysis.router;

import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequest;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponse;
import com.AnalysisAPIserver.domain.analysis.entity.AnalysisLog;
import com.AnalysisAPIserver.domain.analysis.exception.AnalysisException;
import com.AnalysisAPIserver.domain.analysis.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RouterService {

    private final RestTemplate restTemplate;
    private final AnalysisRepository analysisRepository;

    private final String stockAnalysisAPIUrl = "http://{host}/api/analysis/stock/pipeline"; // 임시 호스트

    public AnalysisResponse routeToStockAnalysisAPI(AnalysisRequest request) {
        // 요청을 StockAnalysisAPI로 전달
        try {
            String result = restTemplate.postForObject(
                    stockAnalysisAPIUrl,
                    request,
                    String.class
            );

            // DB 로그 저장
            AnalysisLog log = new AnalysisLog(request.getQuery(), result, request.getClientId());
            analysisRepository.save(log);

            return new AnalysisResponse(result);
        } catch (Exception e) {
            throw new AnalysisException("StockAnalysisAPI 라우팅 실패: " + e.getMessage());
        }
    }
}