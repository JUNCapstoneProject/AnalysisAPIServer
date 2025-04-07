package com.AnalysisAPIserver.domain.auth.controller;

import com.AnalysisAPIserver.domain.auth.dto.*;
import com.AnalysisAPIserver.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new MessageResponse("회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/client-id")
    public ResponseEntity<MessageResponse> issueClientId() {
        String email = getAuthenticatedEmail();
        String result = authService.issueClientIdByEmail(email);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    @GetMapping("/client-id")
    public ResponseEntity<ClientIdResponse> getClientId() {
        String email = getAuthenticatedEmail();
        return ResponseEntity.ok(authService.getClientId(email));
    }

    @DeleteMapping("/client-id")
    public ResponseEntity<MessageResponse> deleteClientId() {
        String email = getAuthenticatedEmail();
        authService.deleteClientId(email);
        return ResponseEntity.ok(new MessageResponse("Client ID 삭제 완료"));
    }

    private String getAuthenticatedEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
