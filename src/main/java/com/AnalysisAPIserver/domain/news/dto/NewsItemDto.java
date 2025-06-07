package com.AnalysisAPIserver.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor; // ⭐️ 추가된 import

/**
 * 뉴스 분석 데이터를 담는 클래스입니다.
 */
@Getter
@RequiredArgsConstructor // ⭐️ final 필드를 초기화하는 생성자를 자동으로 만듭니다.
public final class NewsItemDto {

    /**
     * 이벤트의 종류 ("news") 입니다.
     */
    @JsonProperty("event_type")
    private final String eventType;

    /**
     * 뉴스 분석에 사용될 상세 데이터입니다.
     */
    private final NewsData data;

    /**
     * 뉴스 분석 상세 데이터를 정의하는 내부 클래스입니다.
     */
    @Getter
    @RequiredArgsConstructor // ⭐️ 내부 클래스에도 동일하게 추가합니다.
    public static final class NewsData {

        /**
         * 분석할 뉴스 원문 데이터입니다.
         */
        @JsonProperty("news_data")
        private final String newsData;

        /**
         * 관련 주식의 시세 이력 데이터입니다.
         */
        @JsonProperty("stock_history")
        private final Map<String, List<?>> stockHistory;

        /**
         * 관련 시장 지수 이력 데이터입니다.
         */
        @JsonProperty("market_history")
        private final Map<String, List<?>> marketHistory;

        /**
         * 관련 기업의 손익계산서 데이터입니다.
         */
        @JsonProperty("income_statement")
        private final Map<String, List<?>> incomeStatement;

        /**
         * 기타 정보 데이터입니다.
         */
        private final Map<String, List<?>> info;
    }
}
