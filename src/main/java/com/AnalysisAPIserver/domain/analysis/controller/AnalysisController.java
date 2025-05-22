package com.AnalysisAPIserver.domain.analysis.controller;

import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequestDto;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponseDto;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResultDto;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResultResponseDto;
import com.AnalysisAPIserver.domain.analysis.exception.AnalysisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.AnalysisAPIserver.domain.analysis.service.AnalysisService;

/**
 * 분석 요청을 처리하는 컨트롤러이다.
 */
@Slf4j
@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public final class AnalysisController {
    /**
     * 분석 관련 비즈니스 로직을 처리하는 서비스.
     */
    private final AnalysisService analysisService;

    /**
     * HTTP 상태 코드 400 (Bad Request).
     */
    private static final int STATUS_BAD_REQUEST = 400;
    /**
     * HTTP 상태 코드 500 (Internal Server Error).
     */
    private static final int STATUS_INTERNAL_ERROR = 500;
    /**
     * HTTP 상태 코드 502 (Bad Gateway).
     */
    private static final int STATUS_BAD_GATEWAY = 502;

    /**
     * 분석 요청을 처리하는 메서드이다.
     *
     * @param requestDto 분석 요청 DTO
     * @return 분석 응답 DTO
     */
    @PostMapping("/request")
    public ResponseEntity<AnalysisResponseDto> analysisRequest(
            final @RequestBody AnalysisRequestDto requestDto) {
        try {

            log.info(
                    "[AnalysisController]"
                           + " /api/analysis/request - client_id: {}",
                    requestDto.getBody().getClientId());

            AnalysisResponseDto response =
                    analysisService.forwardToAnalysis(requestDto);


            log.info(
                    "[AnalysisController] Response "
                           + "- status_code: {}, message: {}",
                    response.getStatusCodeVal(),
                    response.getMessage());
            return ResponseEntity.ok(response);
        } catch (AnalysisException e) {
            log.error("Analysis failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    AnalysisResponseDto.builder()
                            .statusCodeVal(STATUS_BAD_REQUEST)
                            .message("Analysis failed: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            return ResponseEntity.internalServerError().body(
                    AnalysisResponseDto.builder()
                            .statusCodeVal(STATUS_INTERNAL_ERROR)
                            .message("Internal Server Error")
                            .build());
        }
    }

    /**
     * 분석 결과를 크롤링 서버로 전송하고, 그 처리 상태를 JSON으로 응답하는 메서드.
     * StockAnalysisAPI가 이 API를 호출합니다.
     *
     * @param resultDto 분석 결과 DTO
     * @return 처리 결과 응답
     */
    @PostMapping("/result")
    public
    ResponseEntity<AnalysisResultResponseDto> sendResult(
            final @RequestBody AnalysisResultDto resultDto) {
        try {
            String responseId = resultDto.getResponseId();
            log.info(
                    "[AnalysisController] /api/analysis/result "
                            + "- Received for response_id: {}",
                    responseId);


            String crawlerResponse
                    = analysisService.sendResultToCrawler(resultDto);

            if (crawlerResponse == null || crawlerResponse.isBlank()) {
                log.warn(
                        "[AnalysisController] "
                               + "No response from server for response_id: {}",
                        responseId);
                AnalysisResultResponseDto responseBody
                        = AnalysisResultResponseDto.builder()
                        .requestId(responseId)
                        .status("Failed to forward result:"
                               + " No response from crawling server.")
                        .build();
                return ResponseEntity
                        .status(HttpStatus.BAD_GATEWAY)
                        .body(responseBody);
            }

            log.info("[Controller] Forwarded result for id: {}"
                          +  ". Crawler response: {}",
                    responseId, crawlerResponse);
            AnalysisResultResponseDto responseBody
                    = AnalysisResultResponseDto.builder()
                    .requestId(responseId)
                    .status("Result successfully forwarded."
                           + " Crawler response: "
                            + crawlerResponse)
                    .build();
            return ResponseEntity
                    .ok(responseBody);

        } catch (Exception e) {
            String responseIdForError =
                    (resultDto != null) ? resultDto.getResponseId() : "Unknown";
            log.error(
                    "[Controller] Failed to send result for id: {}: {}",
                    responseIdForError, e.getMessage(), e);
            AnalysisResultResponseDto errorResponseBody =
                    AnalysisResultResponseDto.builder()
                            .requestId(responseIdForError)
                            .status("Error processing result forwarding: "
                                    + e.getMessage())
                            .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(errorResponseBody);
        }
    }
}
