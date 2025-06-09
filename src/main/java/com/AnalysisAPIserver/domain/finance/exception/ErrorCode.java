package com.AnalysisAPIserver.domain.finance.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 각 에러 상황에 맞는 HTTP 상태 코드와 메시지를 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 클라이언트의 요청 형식이 잘못되었을 경우 발생합니다. (400 Bad Request)
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다. "
            + "요청 파라미터를 확인하세요."),

    /**
     * API 키가 유효하지 않거나 만료되었을 경우 발생합니다. (401 Unauthorized)
     */
    UNAUTHORIZED_API_KEY(HttpStatus.UNAUTHORIZED, "API 키가 유효하지 않거나 만료되었습니다."
            + " API 키를 확인하세요."),

    /**
     * 요청한 리소스를 찾을 수 없을 경우 발생합니다. (404 Not Found)
     */
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND,
            "요청한 종목 코드에 대한 데이터를 찾을 수 없습니다."),

    /**
     * 클라이언트의 요청 횟수가 한도를 초과했을 경우 발생합니다. (429 Too Many Requests)
     */
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "일일 요청 한도를 초과했습니다."
            + " 내일 다시 시도하거나 요금제를 업그레이드하세요."),

    /**
     * 서버 내부에서 예상치 못한 오류가 발생했을 경우입니다. (500 Internal Server Error)
     */
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."
            + " 잠시 후 다시 시도하거나 지원팀에 문의하세요.");

    /**
     * 에러에 해당하는 HTTP 상태 코드입니다.
     */
    private final HttpStatus status;

    /**
     * 클라이언트에게 보여줄 에러 메시지입니다.
     */
    private final String message;
}
