package com.AnalysisAPIserver.domain.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * 재무 분석 데이터를 담는 클래스입니다.
 */
@Getter
public final class FinanceItemDto {

    /**
     * 이벤트의 종류 ("finance") 입니다.
     */
    @JsonProperty("event_type")
    private String eventType;

    /**
     * 재무 분석에 사용될 상세 데이터입니다.
     */
    private FinanceData data;

    /**
     * 재무 분석 상세 데이터를 정의하는 내부 클래스입니다.
     */
    @Getter
    public static final class FinanceData {

        /**
         * 재무상태표 데이터입니다.
         */
        @JsonProperty("balance_sheet")
        private Map<String, List<?>> balanceSheet;

        /**
         * 손익계산서 데이터입니다.
         */
        @JsonProperty("income_statement")
        private Map<String, List<?>> incomeStatement;

        /**
         * 현금흐름표 데이터입니다.
         */
        @JsonProperty("cash_flow")
        private Map<String, List<?>> cashFlow;

        /**
         * 차트 데이터입니다.
         */
        private Map<String, List<?>> chart;
    }
}
