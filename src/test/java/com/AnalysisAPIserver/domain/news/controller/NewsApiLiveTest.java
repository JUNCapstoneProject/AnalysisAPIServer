package com.AnalysisAPIserver.domain.news.controller;



import static org.assertj.core.api.Assertions.assertThat;

import com.AnalysisAPIserver.domain.news.dto.AnalyzeResponseDto;
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
 * [라이브 테스트] 실제 배포된 News API 서버에 요청을 보내는 테스트입니다.
 * 평소에는 @Disabled로 비활성화하고, 필요 시에만 수동으로 실행합니다.
 */
@Disabled // ⭐️ 평소에는 이 테스트가 자동으로 실행되지 않도록 비활성화합니다.
public class NewsApiLiveTest {

    // ⭐️ 스프링이 주입하는 대신, 일반 HTTP 클라이언트를 직접 생성합니다.
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("뉴스 분석 API - 실제 배포 서버 통신 테스트")
    void liveApiCallTest() throws IOException {
        // given (준비)
        // ⭐️ 요청을 보낼 URL을 실제 운영 주소로 하드코딩합니다.
        final String url = "https://developer.tuzain.com/api/news/analyze";

        final String jsonPayload = readJsonFile("payload/news-request.json");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Destination", "analysis"); // Nginx에 필요한 헤더
        final HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        final ParameterizedTypeReference<AnalyzeResponseDto<Object>> responseType =
                new ParameterizedTypeReference<>() { };

        // when (실제 API 호출 실행)
        // ⭐️ 일반 RestTemplate으로 실제 네트워크 요청을 보냅니다.
        final ResponseEntity<AnalyzeResponseDto<Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );

        // then (결과 검증)
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
