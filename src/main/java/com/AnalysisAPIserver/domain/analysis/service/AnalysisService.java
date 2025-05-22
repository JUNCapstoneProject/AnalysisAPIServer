package com.AnalysisAPIserver.domain.analysis.service;

import com.AnalysisAPIserver.domain.analysis.dto.AnalysisRequestDto;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResponseDto;
import com.AnalysisAPIserver.domain.analysis.dto.AnalysisResultDto;
import com.AnalysisAPIserver.domain.analysis.exception.AnalysisException;
import com.AnalysisAPIserver.domain.analysis.router.SocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 분석 서버 라우팅 서비스입니다.
 * 이 클래스는 확장을 고려하지 않았으므로 final로 선언합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {

    /**
     * 소켓 통신을 위한 클라이언트입니다.
     */
    private final SocketClient socketClient;

    /**
     * JSON 직렬화 및 역직렬화를 위한 ObjectMapper 입니다.
     */
    private final ObjectMapper objectMapper;

    /**
     * 주식 분석 API 서버의 호스트 주소입니다.
     */
    private final String stockApiHost = "msiwol.iptime.org";
    /**
     * 주식 분석 API 서버의 포트 번호입니다.
     */
    private final int stockApiPort = 4006;

    /**
     * 크롤러 서버로 결과를 전송할 때 사용되는 포트 번호입니다.
     */
    private static final int CRAWLER_API_PORT = 4005;

    /**
     * 분석 요청을 주식 분석 서버로 전달하고 응답을 받습니다.
     * 이 메소드는 확장을 고려하여 설계되지 않았습니다.
     *
     * @param requestDto 분석 요청 데이터 전송 객체입니다.
     * @return 분석 응답 데이터 전송 객체입니다.
     * @throws AnalysisException 주식 분석 서버로부터 응답이 없거나 파싱에 실패한 경우 발생합니다.
     */
    public AnalysisResponseDto forwardToAnalysis(
            final AnalysisRequestDto requestDto) {

        String responseJson = socketClient.sendMessage(
                stockApiHost, stockApiPort, requestDto);

        if (responseJson == null || responseJson.isBlank()) {
            // LineLength 수정: 가독성을 위해 줄바꿈
            throw new AnalysisException(
                    "No response received from stock analysis server");
        }

        try {

            return objectMapper.readValue(responseJson,
                    AnalysisResponseDto.class);
        } catch (Exception e) {
            log.error("Failed to parse stock analysis response", e);

            throw new AnalysisException(
                    "Failed to parse stock analysis response");
        }
    }

    /**
     * 분석 결과를 크롤러 서버로 전송합니다.
     * 이 메소드는 확장을 고려하여 설계되지 않았습니다.
     *
     * @param resultDto 크롤러로 전송할 분석 결과 데이터 객체입니다.
     * @return 크롤러 서버로부터의 응답 문자열입니다.
     */
    public String sendResultToCrawler(final AnalysisResultDto resultDto) {
        return socketClient.sendMessage(
                "msiwol.iptime.org", CRAWLER_API_PORT, resultDto);
    }
}
