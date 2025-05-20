package com.AnalysisAPIserver.domain.auth.controller;

import com.AnalysisAPIserver.domain.auth.dto.AuthDeveloperInfoResponse;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterRequest;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterResponse;
import com.AnalysisAPIserver.domain.auth.dto.CommonResponse;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
/**
 * 인증 관련 컨트롤러이다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public final class AuthController {

    /**
     * 인증 서비스.
     */


    private final AuthService authService;
    /**
     * 서버 오류 메시지의 최대 표시 길이.
     */
    private static final int MAX_ERROR_MESSAGE_DISPLAY_LENGTH = 70;
    /**
     * 잘린 서버 오류 메시지의 길이 ("..." 제외).
     * MAX_ERROR_MESSAGE_DISPLAY_LENGTH - "...".length()
     */
    private static final int TRUNCATED_ERROR_MESSAGE_LENGTH
            = MAX_ERROR_MESSAGE_DISPLAY_LENGTH - 3;

    /**
     * 개발자 등록 API.
     *
     * @param request 등록 요청 DTO
     * @return 등록 결과
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerDeveloper(
            final @RequestBody AuthRegisterRequest request) {
        try {
            Long developerId = authService.registerDeveloper(request);
            return ResponseEntity.ok(
                    new AuthRegisterResponse(
                            true,
                            new AuthRegisterResponse.ResponseBody(
                                    new AuthRegisterResponse.User(developerId)
                            ),
                            null
                    )
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthRegisterResponse(false,
                            null, e.getMessage()));

        }
    }

    /**
     * 개발자 정보 조회 API.
     *
     * @param bearerToken 인증 토큰
     * @return 개발자 정보
     */
    @GetMapping("/developerId")
    public ResponseEntity<?> getDeveloperInfo(
            final @RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            AuthDeveloperInfoResponse response =
                    authService.getDeveloperInfo(token);
            return ResponseEntity.ok(
                    new CommonResponse<>(true, response, null)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        }
    }

    /**
     * 개발자 삭제 API.
     *
     * @param bearerToken 인증 토큰
     * @param developerId 개발자 ID
     * @return 삭제 결과
     */
    @DeleteMapping("/{developerId}")
    public ResponseEntity<CommonResponse<Void>> deleteDeveloper(
            final @RequestHeader("Authorization") String bearerToken,
            final @PathVariable Long developerId) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            authService.deleteDeveloper(token, developerId);
            return ResponseEntity.ok(
                    new CommonResponse<>(true, null, null)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        }
    }

    /**
     * 로그인 상태 확인 API.
     *
     * @param bearerToken 인증 토큰
     * @return 로그인 상태
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(
            final @RequestHeader("Authorization") String bearerToken) {
        try {
            String accessToken = null;
            String prefix = "Bearer ";

            if (StringUtils.hasText(bearerToken)
                    && bearerToken.startsWith(prefix)) {
                accessToken = bearerToken.substring(prefix.length());
            }

            if (accessToken == null || accessToken.trim().isEmpty()) {
                String message = "Authorization 헤더가 없거나 토큰 형식이 잘못되었습니다.";
                return ResponseEntity.ok(
                        new CommonResponse<>(true,
                                Map.of("loggedIn", false),
                                message)
                );
            }

            boolean isLoggedIn = authService.verifyLoginStatus(accessToken);

            return ResponseEntity.ok(
                    new CommonResponse<>(true,
                            Map.of("loggedIn", isLoggedIn),
                            null)
            );

        } catch (UnauthorizedException e) {
            String errorMessage = "유효하지 않은 토큰입니다: " + e.getMessage();
            // 메시지 길이 제한은 일반 오류 메시지에도 적용할 수 있으나,
            // 주로 서버 내부 오류 메시지에 적용하므로 여기서는 그대로 둡니다.
            // 필요시 아래 Exception 처리와 유사하게 길이 제한 로직 추가 가능.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(false,
                            Map.of("loggedIn", false),
                            errorMessage));
        } catch (Exception e) {
            // 서버 내부 오류 발생 시 메시지 길이 제한
            String serverErrorMessage = "서버에서 오류가 발생했습니다: " + e.getMessage();
            if (serverErrorMessage
                    .length() > MAX_ERROR_MESSAGE_DISPLAY_LENGTH) {
                serverErrorMessage = serverErrorMessage
                        .substring(0, TRUNCATED_ERROR_MESSAGE_LENGTH)
                        + "...";
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>(false,
                            Map.of("loggedIn", false),
                            serverErrorMessage));
        }
    }
}
