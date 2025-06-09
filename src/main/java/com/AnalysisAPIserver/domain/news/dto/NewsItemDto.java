package com.AnalysisAPIserver.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 뉴스 분석 데이터를 담는 클래스입니다.
 * 외부로부터 JSON 데이터를 받아야 하므로 변경 가능하도록 Setter를 추가합니다.
 */
@Getter
@Setter
@NoArgsConstructor // ⭐️ Jackson이 객체를 생성할 수 있도록 기본 생성자 추가
public class NewsItemDto { // ⭐️ final 제거

    /**
     * 이벤트의 종류 ("news") 입니다.
     */
    @JsonProperty("event_type")
    private String eventType; // ⭐️ final 제거

    /**
     * 뉴스 분석에 사용될 상세 데이터입니다.
     */
    private NewsData data; // ⭐️ final 제거

    /**
     * 뉴스 분석 상세 데이터를 정의하는 내부 클래스입니다.
     */
    @Getter
    @Setter
    @NoArgsConstructor // ⭐️ 내부 클래스에도 동일하게 추가
    public static class NewsData { // ⭐️ final 제거

        /**
         * 분석할 뉴스 원문 데이터입니다.
         */
        @JsonProperty("news_data")
        private List<String> newsData; // ⭐️ final 제거 및 List<String> 타입으로 명확화

        /**
         * 관련 주식의 시세 이력 데이터입니다.
         */
        @JsonProperty("stock_history")
        private Map<String, List<?>> stockHistory; // ⭐️ final 제거

        /**
         * 관련 시장 지수 이력 데이터입니다.
         */
        @JsonProperty("market_history")
        private Map<String, List<?>> marketHistory; // ⭐️ final 제거

        /**
         * 관련 기업의 손익계산서 데이터입니다.
         */
        @JsonProperty("income_statement")
        private Map<String, List<?>> incomeStatement; // ⭐️ final 제거

        /**
         * 기타 정보 데이터입니다.
         */
        private Map<String, List<?>> info; // ⭐️ final 제거
    }
}
