package com.AnalysisAPIserver.domain.finance.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.AnalysisAPIserver.domain.finance.dto.AnalyzeResponseDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

/**
 * [라이브 테스트] 실제 배포된 Finance API 서버에 요청을 보내는 테스트입니다.
 */
@Disabled
public class FinanceApiLiveTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("재무 분석 API - 실제 배포 서버 통신 테스트")
    void liveApiCallTest() throws IOException {
        // given
        final String url = "https://developer.tuzain.com/api/finance/analyze";
        final String jsonPayload = readJsonFile("payload/finance-request.json");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Destination", "analysis");
        final HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        final ParameterizedTypeReference<AnalyzeResponseDto<Object>> responseType =
                new ParameterizedTypeReference<>() { };

        // when
        final ResponseEntity<AnalyzeResponseDto<Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        System.out.println("실제 운영 서버 응답 Body: " + response.getBody());
    }

    private String readJsonFile(final String filePath) throws IOException {
        final ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
