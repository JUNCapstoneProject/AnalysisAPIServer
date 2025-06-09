package com.AnalysisAPIserver.domain.news.service;

import com.AnalysisAPIserver.domain.DB_Table.repository.ApplicationRepository;
import com.AnalysisAPIserver.domain.news.dto.AnalyzeRequestDto;
import com.AnalysisAPIserver.domain.news.dto.AnalyzeResponseDto;
import com.AnalysisAPIserver.domain.news.dto.NewsItemDto;
import com.AnalysisAPIserver.domain.news.exception.ApiException;
import com.AnalysisAPIserver.domain.news.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.Zstd;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 뉴스 분석 요청을 받아 내부 API와 통신하고 결과를 반환합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class NewsService {

    /**
     * TCP 소켓 통신에서 사용할 버퍼 크기입니다.
     *
     */
    private static final int SOCKET_BUFFER_SIZE = 4096;

    /**
     * 애플리케이션 인증 정보 조회를 위한 리포지토리.
     */
    private final ApplicationRepository applicationRepository;
    /**
     * JSON 직렬화/역직렬화를 위한 객체.
     */
    private final ObjectMapper objectMapper;

    /**
     * 외부 HTTPS 요청을 받아 내부적으로 TCP 소켓 통신을 수행하는 메인 메소드입니다.
     *
     * @param requestDto 클라이언트로부터 받은 요청 데이터.
     * @return 분석 결과가 담긴 응답 DTO 객체.
     */
    public AnalyzeResponseDto<Object> analyzeViaInternalWebSocket(
            final AnalyzeRequestDto<NewsItemDto> requestDto) {

        final String host = "msiwol.iptime.org";
        final int port = 4006;
        final byte[] endDelimiter = "<END>".getBytes(StandardCharsets.UTF_8);

        Socket socket = null;
        try {
            // 1. 인증
            final String clientId = requestDto.getBody().getClientId();
            final String clientSecret = requestDto.getBody().getClientSecret();
            applicationRepository
                    .findByClientIdAndClientSecret(clientId, clientSecret)
                    .orElseThrow(()
                            -> new ApiException(ErrorCode
                            .UNAUTHORIZED_API_KEY));

            // 2. 데이터 준비 (JSON -> Zstd 압축 -> Base64 인코딩)
            final String itemJson = objectMapper.writeValueAsString(requestDto);
            final byte[] jsonBytes = itemJson.getBytes(StandardCharsets.UTF_8);
            final byte[] compressedData = Zstd.compress(jsonBytes);
            log.info("Payload compressed: {} bytes -> {} bytes",
                    jsonBytes.length, compressedData.length);
            final byte[] base64Encoded
                    = Base64.getEncoder().encode(compressedData);

            // 3. 최종 전송 데이터 생성 (인코딩된 데이터 + 구분자)
            final ByteArrayOutputStream messageStream
                    = new ByteArrayOutputStream();
            messageStream.write(base64Encoded);
            messageStream.write(endDelimiter);
            final byte[] messageToSend = messageStream.toByteArray();

            // 4. TCP 소켓 연결 및 데이터 전송
            socket = new Socket(host, port);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            log.info("TCP connection established. Sending data ({} bytes)...",
                    messageToSend.length);
            os.write(messageToSend);
            os.flush();

            // 5. 응답 수신 (응답은 평범한 JSON으로 가정)
            log.info("Waiting for response from TCP server...");
            final ByteArrayOutputStream responseStream
                    = new ByteArrayOutputStream();
            final byte[] buffer = new byte[SOCKET_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                responseStream.write(buffer, 0, bytesRead);
            }

            final byte[] responseData = responseStream.toByteArray();
            log.info("Received {} bytes from server.", responseData.length);

            // 6. 응답 데이터 처리 및 반환
            byte[] jsonResponseBytes
                    = removeDelimiter(responseData, endDelimiter);
            final TypeReference<AnalyzeResponseDto<Object>>
                    typeRef = new TypeReference<>() { };
            return objectMapper.readValue(jsonResponseBytes, typeRef);

        } catch (ApiException e) {
            return createErrorDto(e.getErrorCode());
        } catch (Exception e) {
            log.error("내부 TCP 통신 중 오류 발생", e);
            return createErrorDto(ErrorCode.SERVER_ERROR);
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Socket close error", e);
                }
            }
        }
    }

    /**
     * 응답 바이트 배열 끝에 구분자가 있다면 안전하게 제거하는 헬퍼 메소드입니다.
     *
     * @param data      원본 바이트 배열.
     * @param delimiter 제거할 구분자 바이트 배열.
     * @return 구분자가 제거된 바이트 배열.
     */
    private byte[] removeDelimiter(final byte[] data, final byte[] delimiter) {
        if (data == null || delimiter == null
                || data.length < delimiter.length) {
            return data;
        }

        int start = data.length - delimiter.length;
        boolean delimiterFound = true;
        for (int i = 0; i < delimiter.length; i++) {
            if (data[start + i] != delimiter[i]) {
                delimiterFound = false;
                break;
            }
        }
        return delimiterFound ? Arrays.copyOf(data, start) : data;
    }

    /**
     * 에러 응답 DTO를 생성하는 도우미 메소드입니다.
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
