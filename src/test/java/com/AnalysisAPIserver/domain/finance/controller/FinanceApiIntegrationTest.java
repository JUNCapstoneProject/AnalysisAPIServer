package com.AnalysisAPIserver.domain.finance.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.AnalysisAPIserver.domain.finance.dto.AnalyzeResponseDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

/**
 * 실제 서버를 띄워 Finance API의 End-to-End 통신을 테스트하는 클래스입니다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class FinanceApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("재무 분석 API - 실제 통신 통합 테스트")
    void fullIntegrationTest() throws IOException {
        // given (준비)
        final String url = "http://localhost:" + port + "/api/finance/analyze";
        final String jsonPayload = readJsonFile("payload/finance-request.json");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Nginx가 필요로 하는 Destination 헤더가 있다면 추가합니다.
        // headers.add("Destination", "analysis");

        final HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        // 제네릭 타입을 포함한 응답 타입을 명확하게 정의합니다.
        final ParameterizedTypeReference<AnalyzeResponseDto<Object>> responseType =
                new ParameterizedTypeReference<>() { };

        // when (실제 API 호출 실행)
        final ResponseEntity<AnalyzeResponseDto<Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );

        // then (결과 검증)
        // 실제 stockanalysisAPI 서버의 응답에 따라 검증 코드를 작성해야 합니다.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Success");

        System.out.println("실제 응답 Body: " + response.getBody());
    }

    private String readJsonFile(final String filePath) throws IOException {
        final ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
