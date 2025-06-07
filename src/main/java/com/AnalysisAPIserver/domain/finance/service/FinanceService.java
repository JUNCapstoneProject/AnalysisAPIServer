package com.AnalysisAPIserver.domain.finance.service;

import com.AnalysisAPIserver.domain.DB_Table.repository.ApplicationRepository;
import com.AnalysisAPIserver.domain.finance.dto.AnalyzeRequestDto;
import com.AnalysisAPIserver.domain.finance.dto.AnalyzeResponseDto;
import com.AnalysisAPIserver.domain.finance.dto.FinanceItemDto;
import com.AnalysisAPIserver.domain.finance.exception.ApiException;
import com.AnalysisAPIserver.domain.finance.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


/**
 * 재무 분석 요청을 받아 내부 API와 통신하고 결과를 반환합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    /**
     * 내부 웹소켓 통신의 타임아웃 시간(초)입니다.
     */
    private static final long WEBSOCKET_TIMEOUT_SECONDS = 30L;

    /**
     * DB의 Application 테이블에 접근하기 위한 리포지토리입니다.
     */
    private final ApplicationRepository applicationRepository;
    /**
     * Java 객체와 JSON 간의 변환을 처리하는 객체입니다.
     */
    private final ObjectMapper objectMapper;

    /**
     * 외부 HTTPS 요청을 받아 내부적으로 웹소켓 통신을 수행하는 메인 메소드.
     *
     * @param requestDto 클라이언트로부터 받은 요청 데이터.
     * @return 분석 결과가 담긴 응답 DTO 객체.
     */
    public AnalyzeResponseDto<Object> analyzeViaInternalWebSocket(
            final AnalyzeRequestDto<FinanceItemDto> requestDto) {
        try {
            // 1. 클라이언트 인증
            final String clientId = requestDto.getBody().getClientId();
            final String clientSecret = requestDto.getBody().getClientSecret();
            applicationRepository.findByClientIdAndClientSecret(
                            clientId, clientSecret)
                    .orElseThrow(() ->
                            new ApiException(ErrorCode.UNAUTHORIZED_API_KEY));

            // 2. 내부 웹소켓 서버와 통신 시작
            final String internalWsUrl = "ws://msiwol.iptime.org:4006";
            final CompletableFuture<AnalyzeResponseDto<Object>> futureResponse =
                    new CompletableFuture<>();

            final InternalApiHandler internalApiHandler
                    = new InternalApiHandler(
                    futureResponse,
                    objectMapper,
                    requestDto.getBody().getItem()
            );

            final StandardWebSocketClient webSocketClient =
                    new StandardWebSocketClient();
            webSocketClient.execute(internalApiHandler, internalWsUrl);

            // 3. 내부 서버로부터 응답이 올 때까지 대기
            return futureResponse.get(
                    WEBSOCKET_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        } catch (ApiException e) {
            return createErrorDto(e.getErrorCode());
        } catch (Exception e) {
            log.error("내부 웹소켓 통신 중 오류 발생", e);
            return createErrorDto(ErrorCode.SERVER_ERROR);
        }
    }

    /**
     * 내부 stockanalysisAPI 와의 통신을 전담하는 핸들러 클래스.
     */
    @Slf4j
    @RequiredArgsConstructor
    private static class InternalApiHandler extends TextWebSocketHandler {
        /**
         * 비동기 처리 결과를 담을 CompletableFuture 객체입니다.
         */
        private final
        CompletableFuture<AnalyzeResponseDto<Object>> futureResponse;
        /**
         * JSON 변환을 위한 ObjectMapper 객체입니다.
         */
        private final ObjectMapper objectMapper;
        /**
         * 내부 API 서버로 전송할 재무 데이터 아이템입니다.
         */
        private final FinanceItemDto itemToSend;

        @Override
        public void afterConnectionEstablished(final WebSocketSession session)
                throws Exception {
            log.info("내부 API 서버와 웹소켓 연결 성공. 데이터 전송 시작: {}",
                    session.getId());
            final String payload = objectMapper.writeValueAsString(itemToSend);
            session.sendMessage(new TextMessage(payload));
        }

        @Override
        protected void handleTextMessage(final WebSocketSession session,
                                         final TextMessage message)
                throws Exception {
            log.info("내부 API 서버로부터 응답 수신.");
            final String payload = message.getPayload();
            final TypeReference<AnalyzeResponseDto<Object>> typeRef =
                    new TypeReference<>() { };
            final AnalyzeResponseDto<Object> response =
                    objectMapper.readValue(payload, typeRef);

            futureResponse.complete(response);
            session.close(CloseStatus.NORMAL);
        }

        @Override
        public void handleTransportError(final WebSocketSession session,
                                         final Throwable exception)
                throws Exception {
            log.error("내부 API 통신 중 전송 오류 발생", exception);
            futureResponse.completeExceptionally(exception);
            super.handleTransportError(session, exception);
        }
    }

    /**
     * 에러 응답 DTO를 생성하는 도우미 메소드.
     *
     * @param errorCode 발생한 에러의 종류를 담은 ErrorCode 객체.
     * @return 에러 정보가 포함된 응답 DTO 객체.
     */
    private AnalyzeResponseDto<Object>
    createErrorDto(final ErrorCode errorCode) {
        return AnalyzeResponseDto.builder()
                .responseId(UUID.randomUUID().toString())
                .statusCode(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
