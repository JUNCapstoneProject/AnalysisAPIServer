package com.AnalysisAPIserver.domain.news.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.AnalysisAPIserver.domain.news.dto.AnalyzeRequestDto
import com.AnalysisAPIserver.domain.news.dto.AnalyzeResponseDto;
import com.AnalysisAPIserver.domain.news.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * NewsController에 대한 웹 계층 슬라이스 테스트 클래스입니다.
 */
@WebMvcTest(controllers = NewsController.class)
final class NewsControllerTest {

    /**
     * HTTP 요청을 시뮬레이션하기 위한 Spring test 유틸리티입니다.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Java 객체를 JSON 문자열로 변환하거나, 그 반대를 수행하기 위한 객체입니다.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 실제 NewsService를 대체하는 가짜(Mock) 객체입니다.
     * 이를 통해 서비스 계층의 로직과 상관없이 컨트롤러의 동작만 독립적으로 테스트할 수 있습니다.
     */
    @MockBean
    private NewsService newsService;

    @Test
    @DisplayName("뉴스 분석 API 호출 시, 성공적으로 응답(200 OK)을 반환한다")
    void analyzeNewsSuccessTest() throws Exception {
        // given (테스트 준비)
        // 1. newsService의 analyzeViaInternalWebSocket 메소드가 어떤 값이 들어오든(any()),
        //    미리 정의된 성공 응답을 반환하도록 설정합니다.
        given(newsService.analyzeViaInternalWebSocket(any()))
                .willReturn(createSuccessResponse());

        // 2. 컨트롤러에 전송할 실제 요청 본문(JSON)을 Map을 이용해 생성합니다.
        //    이렇게 하면 DTO의 생성자나 Setter 없이도 테스트가 가능합니다.
        final Map<String, Object> requestPayload = Map.of(
                "header", Map.of("Content-Type", "application/json"),
                "body", Map.of(
                        "client_id", "937041b0-201e-4797-95cc-750655f288dc",
                        "client_secret", "75e50882-a51f-45dd-b9b5-d92fee0d0f7c",
                        "item", Map.of(
                                "event_type", "news",
                                "data", Map.of(
                                        "news_data", "오늘 주식 시장은 매우 긍정적입니다.",
                                        "stock_history", Collections.emptyMap()
                                )
                        )
                )
        );

        // 3. 생성한 Map을 JSON 문자열로 변환합니다.
        final String jsonPayload = objectMapper.writeValueAsString(requestPayload);


        // when (실제 API 호출 실행)
        final ResultActions resultActions = mockMvc.perform(
                post("/api/news/analyze") // 이 경로로
                        .contentType(MediaType.APPLICATION_JSON) // JSON 타입으로
                        .content(jsonPayload) // 이 내용을 담아 POST 요청을 보냅니다.
        );


        // then (결과 검증)
        resultActions
                .andExpect(status().isOk()) // HTTP 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.statusCode").value(200)) // 응답 본문의 statusCode 필드 값 확인
                .andExpect(jsonPath("$.message").value("분석에 성공했습니다."))
                .andExpect(json_path("$.item.eventType").value("news"))
                .andDo(print()); // 요청과 응답의 전체 내용을 콘솔에 출력하여 확인
    }

    /**
     * Mock Service가 반환할 성공 응답 DTO를 생성하는 헬퍼 메소드입니다.
     *
     * @return 성공 시의 AnalyzeResponseDto 객체.
     */
    private AnalyzeResponseDto<Object> createSuccessResponse() {
        final var responseItem =
                AnalyzeResponseDto.ResponseItem.builder()
                        .eventType("news")
                        .result(Map.of("sentiment", "positive", "score", 0.95))
                        .build();

        return AnalyzeResponseDto.builder()
                .responseId(UUID.randomUUID().toString())
                .statusCode(200)
                .message("분석에 성공했습니다.")
                .item(responseItem)
                .build();
    }
}