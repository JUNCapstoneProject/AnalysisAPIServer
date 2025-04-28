package com.AnalysisAPIserver.domain.auth.controller;

import com.AnalysisAPIserver.domain.auth.dto.ClientIdResponse;
import com.AnalysisAPIserver.domain.auth.dto.LoginRequest;
import com.AnalysisAPIserver.domain.auth.dto.LoginResponse;
import com.AnalysisAPIserver.domain.auth.dto.MessageResponse;
import com.AnalysisAPIserver.domain.auth.dto.RegisterRequest;
import com.AnalysisAPIserver.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 인증 서비스.
     */
    private final AuthService authService;

    /**
     * 회원가입을 처리한다.
     *
     * @param request 회원가입 요청
     * @return 성공 메시지
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @RequestBody final RegisterRequest request) {
        this.authService.register(request);
        return ResponseEntity.ok(new MessageResponse("회원가입 성공"));
    }

    /**
     * 로그인 요청을 처리한다.
     *
     * @param request 로그인 요청
     * @return 로그인 응답
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(this.authService.login(request));
    }

    /**
     * Client ID 발급 요청을 처리한다.
     *
     * @return 발급된 Client ID
     */
    @PostMapping("/client-id")
    public ResponseEntity<MessageResponse> issueClientId() {
        String email = getAuthenticatedEmail();
        String result = this.authService.issueClientIdByEmail(email);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    /**
     * Client ID 조회 요청을 처리한다.
     *
     * @return Client ID 정보
     */
    @GetMapping("/client-id")
    public ResponseEntity<ClientIdResponse> getClientId() {
        String email = getAuthenticatedEmail();
        return ResponseEntity.ok(this.authService.getClientId(email));
    }

    /**
     * Client ID 삭제 요청을 처리한다.
     *
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/client-id")
    public ResponseEntity<MessageResponse> deleteClientId() {
        String email = getAuthenticatedEmail();
        this.authService.deleteClientId(email);
        return ResponseEntity.ok(new MessageResponse("Client ID 삭제 완료"));
    }

    /**
     * 현재 인증된 사용자의 이메일을 가져온다.
     *
     * @return 이메일
     */
    private String getAuthenticatedEmail() {
        return (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
