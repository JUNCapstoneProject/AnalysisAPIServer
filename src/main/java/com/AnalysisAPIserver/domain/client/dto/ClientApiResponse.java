package com.AnalysisAPIserver.domain.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트 API의 공통 응답 형식을 정의하는 DTO입니다.
 * 성공 여부, 실제 응답 데이터, 에러 정보를 포함합니다.
 *
 * @param <T> 응답 데이터의 타입
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiResponse<T> {
    /**
     * API 호출 성공 여부입니다.
     */
    private boolean success;
    /**
     * API 호출 성공 시 응답 데이터입니다.
     */
    private T response;
    /**
     * API 호출 실패 시 에러 정보입니다.
     */
    private ClientApiError error;

    /**
     * 성공 응답 객체를 생성합니다.
     *
     * @param response 응답 데이터
     * @param <T>      응답 데이터의 타입
     * @return 성공 상태의 ClientApiResponse 객체
     */
    public static <T> ClientApiResponse<T> success(final T response) {
        return new ClientApiResponse<>(true, response, null);
    }

    /**
     * 실패 응답 객체를 생성합니다.
     *
     * @param error 에러 정보
     * @param <T>   응답 데이터의 타입 (실패 시 null)
     * @return 실패 상태의 ClientApiResponse 객체
     */
    public static <T> ClientApiResponse<T> error(final ClientApiError error) {
        return new ClientApiResponse<>(false, null, error);
    }
}
