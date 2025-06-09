package com.AnalysisAPIserver.domain.news.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.AnalysisAPIserver.domain.news.dto.AnalyzeResponseDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference; // ⭐️ 1. import 추가
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class NewsApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("뉴스 분석 API - 실제 통신 통합 테스트")
    void fullIntegrationTest() throws IOException {
        // given
        final String url = "http://localhost:" + port + "/api/news/analyze";
        final String jsonPayload = readJsonFile("payload/news-request.json");
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
        // ⭐️ 서버의 실제 응답 메시지인 "Success"를 기대하도록 수정
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