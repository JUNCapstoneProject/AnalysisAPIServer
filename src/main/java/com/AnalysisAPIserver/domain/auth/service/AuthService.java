package com.AnalysisAPIserver.domain.auth.service;

import com.AnalysisAPIserver.common.security.JwtUtil;
import com.AnalysisAPIserver.domain.auth.dto.ClientIdResponse;
import com.AnalysisAPIserver.domain.auth.dto.LoginRequest;
import com.AnalysisAPIserver.domain.auth.dto.LoginResponse;
import com.AnalysisAPIserver.domain.auth.dto.RegisterRequest;
import com.AnalysisAPIserver.domain.auth.entity.ApiKey;
import com.AnalysisAPIserver.domain.auth.entity.User;
import com.AnalysisAPIserver.domain.auth.exception.*;
import com.AnalysisAPIserver.domain.auth.repository.ApiKeyRepository;
import com.AnalysisAPIserver.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException("이미 존재하는 이메일입니다.");
        }

        User user = new User(request.getEmail(), request.getPassword());
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("유저를 찾을 수 없습니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token);
    }

    public String issueClientIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
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

        apiKeyRepository.save(apiKey);
        return clientId;
    }

    public ClientIdResponse getClientId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("유저를 찾을 수 없습니다."));

        ApiKey apiKey = user.getApiKey();
        if (apiKey == null) {
            throw new AuthException("발급된 API 키가 없습니다.");
        }

        return new ClientIdResponse(apiKey.getClientId(), apiKey.getClientSecret());
    }

    public void deleteClientId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("유저를 찾을 수 없습니다."));

        ApiKey apiKey = user.getApiKey();
        if (apiKey != null) {
            apiKeyRepository.delete(apiKey);
        }
    }
}
