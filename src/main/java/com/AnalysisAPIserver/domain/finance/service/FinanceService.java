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
import java.util.UUID;
import com.github.luben.zstd.Zstd;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * finance 서비스 정의.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class FinanceService {

    /**
     * TCP 소켓 통신에서 사용할 버퍼 크기.
     */
    private static final int SOCKET_BUFFER_SIZE = 4096;

    /**
     * 애플리케이션 정보 조회를 위한 리포지토리.
     */
    private final ApplicationRepository applicationRepository;
    /**
     * JSON 직렬화/역직렬화를 위한 객체.
     */
    private final ObjectMapper objectMapper;

    /**
     * 내부 TCP 소켓 통신을 통해 금융 데이터 분석을 요청하고 응답을 받습니다.
     *
     * @param requestDto 클라이언트로부터 받은 분석 요청 데이터
     * @return 분석 결과를 담은 응답 DTO
     */
    public AnalyzeResponseDto<Object> analyzeViaInternalWebSocket(
            final AnalyzeRequestDto<FinanceItemDto> requestDto) {

        final String host = "msiwol.iptime.org";
        final int port = 4006;
        final byte[] endDelimiter = "<END>".getBytes(StandardCharsets.UTF_8);

        Socket socket = null;
        try {
            // 인증
            final String clientId = requestDto.getBody().getClientId();
            final String clientSecret = requestDto.getBody().getClientSecret();
            applicationRepository
                    .findByClientIdAndClientSecret(clientId,
                            clientSecret)
                    .orElseThrow(()
                            -> new ApiException(ErrorCode
                            .UNAUTHORIZED_API_KEY));

            // 데이터 준비 (JSON -> Zstd 압축 -> Base64 인코딩)
            final String itemJson = objectMapper.writeValueAsString(requestDto);
            final byte[] jsonBytes = itemJson.getBytes(StandardCharsets.UTF_8);
            final byte[] compressedData = Zstd.compress(jsonBytes);
            log.info("Payload compressed: {} bytes -> {} bytes",
                    jsonBytes.length, compressedData.length);
            final byte[] base64Encoded
                    = Base64.getEncoder().encode(compressedData);

            final ByteArrayOutputStream messageStream
                    = new ByteArrayOutputStream();
            messageStream.write(base64Encoded);
            messageStream.write(endDelimiter);
            final byte[] messageToSend = messageStream.toByteArray();

            // TCP 소켓 통신
            socket = new Socket(host, port);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            log.info("TCP connection established. Sending data...");
            os.write(messageToSend);
            os.flush();

            // 응답 수신
            final ByteArrayOutputStream responseStream
                    = new ByteArrayOutputStream();
            final byte[] buffer = new byte[SOCKET_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                responseStream.write(buffer, 0, bytesRead);
            }

            final byte[] responseData = responseStream.toByteArray();
            byte[] jsonResponseBytes
                    = removeDelimiter(responseData, endDelimiter);
            final TypeReference<AnalyzeResponseDto<Object>> typeRef =
                    new TypeReference<>() { };
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

    private AnalyzeResponseDto<Object>
    createErrorDto(final ErrorCode errorCode) {
        return AnalyzeResponseDto.builder()
                .responseId(UUID.randomUUID().toString())
                .statusCode(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
