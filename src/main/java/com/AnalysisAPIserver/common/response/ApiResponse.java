package com.AnalysisAPIserver.common.response;

import lombok.Getter;

/**
 * API 응답을 표준화하는 클래스이다.
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
public final class ApiResponse<T> {

    /**
     * 성공 여부.
     */
    private final boolean success;

    /**
     * 응답 데이터.
     */
    private final T data;

    /**
     * 응답 메시지.
     */
    private final String message;

    private ApiResponse(
            final boolean successParam,
            final T dataParam,
            final String messageParam) {
        this.success = successParam;
        this.data = dataParam;
        this.message = messageParam;
    }

    /**
     * 성공 응답을 생성한다.
     *
     * @param <T> 응답 데이터 타입
     * @param data 응답 데이터
     * @return 성공 ApiResponse
     */
    public static <T> ApiResponse<T> success(final T data) {
        return new ApiResponse<>(true, data, null);
    }

    /**
     * 실패 응답을 생성한다.
     *
     * @param message 실패 메시지
     * @return 실패 ApiResponse
     */
    public static ApiResponse<?> fail(final String message) {
        return new ApiResponse<>(false, null, message);
    }
}
