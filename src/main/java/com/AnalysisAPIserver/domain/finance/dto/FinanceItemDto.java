package com.AnalysisAPIserver.domain.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FinanceItemDto {

    /**
     * 이벤트 타입.
     */
    @JsonProperty("event_type")
    private String eventType;

    /**
     * 재무 데이터.
     */
    private FinanceData data;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class FinanceData {
        /**
         * 재무상태표 데이터.
         */
        @JsonProperty("balance_sheet")
        private Map<String, List<?>> balanceSheet;

        /**
         * 손익계산서 데이터.
         */
        @JsonProperty("income_statement")
        private Map<String, List<?>> incomeStatement;

        /**
         * 현금흐름표 데이터.
         */
        @JsonProperty("cash_flow")
        private Map<String, List<?>> cashFlow;

        /**
         * 차트 데이터.
         */
        private Map<String, List<?>> chart;
    }
}
