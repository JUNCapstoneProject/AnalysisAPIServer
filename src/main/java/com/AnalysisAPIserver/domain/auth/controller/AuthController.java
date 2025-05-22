package com.AnalysisAPIserver.domain.auth.controller;

import com.AnalysisAPIserver.domain.auth.dto.AuthDeveloperInfoResponse;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterRequest;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterResponse;
import com.AnalysisAPIserver.domain.auth.dto.CommonResponse;
import com.AnalysisAPIserver.domain.auth.exception.ResourceNotFoundException;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 컨트롤러이다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public final class AuthController {

    /**
     * 로거 객체.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(AuthController.class);
    /**
     * 인증 서비스 로직을 처리하는 서비스.
     */
    private final AuthService authService;

    /**
     * 개발자 등록을 처리합니다.
     *
     * @param request 등록 요청 정보
     * @return 등록 결과 응답
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
            LOGGER.warn("/register - UnauthorizedException: {}",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthRegisterResponse(false,
                            null, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("/register - Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthRegisterResponse(false, null,
                            "서버 내부 오류가 발생했습니다."));
        }
    }

    /**
     * 특정 개발자 ID의 정보를 조회합니다.
     * 요청자는 Authorization 헤더의 토큰을 통해 인증되어야 하며, 자신의 정보만 조회 가능합니다.
     *
     * @param developerId 조회할 개발자의 ID (URL 경로 변수).
     * @param bearerToken 인증 토큰 ("Bearer " 접두사 포함).
     * @return 개발자 정보 (userName은 이메일).
     */
    @GetMapping("/{developerId}")
    public ResponseEntity<?> getDeveloperInfo(
            @PathVariable final Long developerId,
            final @RequestHeader("Authorization") String bearerToken) {
        try {
            AuthDeveloperInfoResponse response =
                    authService.getDeveloperInfo(bearerToken, developerId);
            return ResponseEntity.ok(
                    new CommonResponse<>(true, response, null)
            );
        } catch (ResourceNotFoundException e) {
            LOGGER.warn("/api/auth/{} - ResourceNotFoundException: {}",
                    developerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        } catch (UnauthorizedException e) { // 토큰 문제 또는 권한 문제
            LOGGER.warn("/api/auth/{} - UnauthorizedException: {}",
                    developerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("/api/auth/{} - Unexpected error: ", developerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>(false, null,
                            "서버에서 오류가 발생했습니다."));
        }
    }

    /**
     * 개발자 정보를 삭제합니다.
     *
     * @param bearerToken 인증 토큰
     * @param developerId 삭제할 개발자 ID
     * @return 삭제 결과 응답
     */
    @DeleteMapping("/{developerId}")
    public ResponseEntity<CommonResponse<Void>> deleteDeveloper(
            final @RequestHeader("Authorization") String bearerToken,
            final @PathVariable Long developerId) {
        try {
            authService.deleteDeveloper(bearerToken, developerId);
            return ResponseEntity.ok(
                    new CommonResponse<>(true, null, null)
            );
        } catch (ResourceNotFoundException e) {
            LOGGER.warn("/delete/{} - ResourceNotFoundException: {}",
                    developerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        } catch (UnauthorizedException e) {
            LOGGER.warn("/delete/{} - UnauthorizedException: {}",
                    developerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(false, null, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("/delete/{} - Unexpected error: ", developerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>(false, null,
                            "서버 내부 오류가 발생했습니다."));
        }
    }

    /**
     * 로그인 상태를 확인합니다.
     *
     * @param bearerToken 인증 토큰
     * @return 로그인 상태 확인 결과 응답
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(
            final @RequestHeader("Authorization") String bearerToken) {
        try {
            boolean isLoggedIn = authService.verifyLoginStatus(bearerToken);
            return ResponseEntity.ok(
                    new CommonResponse<>(true,
                            Map.of("loggedIn", isLoggedIn), null)
            );
        } catch (UnauthorizedException e) {
            LOGGER.warn("/check - UnauthorizedException: {}", e.getMessage());
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(false, Map.of("loggedIn", false),
                            errorMessage));
        } catch (Exception e) {
            LOGGER.error("/check - Unexpected error: ", e);
            String serverErrorMessage = "서버에서 오류가 발생했습니다.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>(false, Map.of("loggedIn", false),
                            serverErrorMessage));
        }
    }
}
