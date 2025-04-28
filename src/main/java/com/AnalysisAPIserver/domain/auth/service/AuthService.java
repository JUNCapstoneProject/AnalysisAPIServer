package com.AnalysisAPIserver.domain.auth.service;

import com.AnalysisAPIserver.common.security.JwtUtil;
import com.AnalysisAPIserver.domain.auth.dto.ClientIdResponse;
import com.AnalysisAPIserver.domain.auth.dto.LoginRequest;
import com.AnalysisAPIserver.domain.auth.dto.LoginResponse;
import com.AnalysisAPIserver.domain.auth.dto.RegisterRequest;
import com.AnalysisAPIserver.domain.auth.entity.ApiKey;
import com.AnalysisAPIserver.domain.auth.entity.User;
import com.AnalysisAPIserver.domain.auth.exception.AuthException;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.repository.ApiKeyRepository;
import com.AnalysisAPIserver.domain.auth.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스이다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * 사용자 리포지토리.
     */
    private final UserRepository userRepository;

    /**
     * API 키 리포지토리.
     */
    private final ApiKeyRepository apiKeyRepository;

    /**
     * JWT 유틸.
     */
    private final JwtUtil jwtUtil;

    /**
     * 회원가입을 처리한다.
     *
     * @param request 회원가입 요청
     */
    public void register(final RegisterRequest request) {
        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException("이미 존재하는 이메일입니다.");
        }
        User user = new User(request.getEmail(), request.getPassword());
        this.userRepository.save(user);
    }

    /**
     * 로그인 요청을 처리한다.
     *
     * @param request 로그인 요청
     * @return 로그인 응답
     */
    public LoginResponse login(final LoginRequest request) {
        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("유저를 찾을 수 없습니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        String token = this.jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token);
    }

    /**
     * 이메일로 Client ID를 발급한다.
     *
     * @param email 이메일
     * @return 발급된 Client ID
     */
    public String issueClientIdByEmail(final String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("유저를 찾을 수 없습니다."));

        if (user.getApiKey() != null) {
            throw new AuthException("이미 API 키가 발급되어 있습니다.");
        }

        String clientId = UUID.randomUUID().toString();
        String clientSecret = UUID.randomUUID().toString();

        ApiKey apiKey = new ApiKey();
        apiKey.setClientId(clientId);
        apiKey.setClientSecret(clientSecret);
        apiKey.setUser(user);

        this.apiKeyRepository.save(apiKey);
        return clientId;
    }

    /**
     * 이메일로 Client ID 정보를 조회한다.
     *
     * @param email 이메일
     * @return Client ID 응답
     */
    public ClientIdResponse getClientId(final String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("유저를 찾을 수 없습니다."));

        ApiKey apiKey = user.getApiKey();
        if (apiKey == null) {
            throw new AuthException("발급된 API 키가 없습니다.");
        }

        return new ClientIdResponse(apiKey.getClientId(),
                apiKey.getClientSecret());
    }

    /**
     * 이메일로 Client ID를 삭제한다.
     *
     * @param email 이메일
     */
    public void deleteClientId(final String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("유저를 찾을 수 없습니다."));

        ApiKey apiKey = user.getApiKey();
        if (apiKey != null) {
            this.apiKeyRepository.delete(apiKey);
        }
    }
}
