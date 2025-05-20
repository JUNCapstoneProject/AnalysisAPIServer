package com.AnalysisAPIserver.domain.auth.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 공통 응답 DTO.
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    /**
     * 성공 여부.
     */
    private boolean success;

    /**
     * 응답 본문.
     */
    private T response;

    /**
     * 오류 메시지.
     */
    private String error;
}
