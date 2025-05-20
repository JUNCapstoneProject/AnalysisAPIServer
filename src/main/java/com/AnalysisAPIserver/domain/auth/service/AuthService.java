package com.AnalysisAPIserver.domain.auth.service;


import com.AnalysisAPIserver.domain.auth.dto.AuthDeveloperInfoResponse;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterRequest;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.jwt.JwtTokenProvider;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 개발자 등록, 정보 조회, 삭제 등의 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService { // 클래스는 final로 선언

    /**
     * API 사용자 정보에 접근하기 위한 리포지토리입니다.
     */
    private final ApiUserRepository apiUserRepository;
    /**
     * JWT 토큰 생성, 검증 및 클레임 추출을 담당하는 프로바이더입니다.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 주어진 액세스 토큰을 사용하여 개발자가 존재하지 않으면 새로 등록합니다.
     * 토큰이 유효하지 않으면 {@link IllegalArgumentException}을 발생시킵니다.
     *
     * @param accessToken 검증 및 사용자 정보 추출에 사용될 JWT 액세스 토큰.
     */
    @Transactional
    public void registerIfNotExists(final String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException(
                    "Invalid JWT token.");
        }

        Long developerId = jwtTokenProvider.getDeveloperId(accessToken);
        if (!apiUserRepository.existsById(developerId)) {
            ApiUser apiUser = ApiUser.builder()
                    .developerId(developerId)
                    .userName(jwtTokenProvider.getUserName(accessToken))
                    .userType("개발자")
                    .createdAt(LocalDateTime.now())
                    .build();
            apiUserRepository.save(apiUser);
        }
    }

    /**
     * 주어진 액세스 토큰을 사용하여 개발자 정보를 조회합니다.
     * 토큰이 유효하지 않거나 등록되지 않은 개발자인 경우 {@link UnauthorizedException}을 발생시킵니다.
     *
     * @param accessToken 개발자 정보 조회에 사용될 JWT 액세스 토큰.
     * @return 조회된 개발자 정보를 담은 {@link AuthDeveloperInfoResponse} 객체.
     */
    @Transactional(readOnly = true)
    public
    AuthDeveloperInfoResponse getDeveloperInfo(final String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        Long developerId = jwtTokenProvider.getDeveloperId(accessToken);
        ApiUser user = apiUserRepository.findById(developerId)
                .orElseThrow(()
                        -> new UnauthorizedException("등록되지 않은 개발자입니다."));

        AuthDeveloperInfoResponse.User userDto
                = new AuthDeveloperInfoResponse.User(
                user.getUserName(),
                user.getUserType(),
                user.getCreatedAt().toLocalDate()
        );

        return AuthDeveloperInfoResponse.builder()
                .user(userDto)
                .build();
    }

    /**
     * 주어진 액세스 토큰과 개발자 ID를 사용하여 개발자 정보를 삭제합니다.
     * 토큰이 유효하지 않거나, 토큰의 개발자 ID와 요청된 개발자 ID가 일치하지 않거나,
     * 존재하지 않는 개발자인 경우 {@link UnauthorizedException} 또는
     * {@link IllegalArgumentException}을 발생시킵니다.
     *
     * @param accessToken 개발자 삭제 권한 검증에 사용될 JWT 액세스 토큰.
     * @param developerId 삭제할 개발자의 ID.
     */
    @Transactional
    public void deleteDeveloper(final String accessToken,
                                final Long developerId) { // FinalParameters 적용
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        Long tokenDeveloperId = jwtTokenProvider.getDeveloperId(accessToken);
        if (!tokenDeveloperId.equals(developerId)) {
            throw new UnauthorizedException("본인의 계정만 삭제할 수 있습니다.");
        }

        ApiUser user = apiUserRepository.findById(developerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 개발자입니다.")); // LineLength 해결

        apiUserRepository.delete(user);
    }

    /**
     * 주어진 {@link AuthRegisterRequest}의 액세스 토큰을 사용하여 개발자를 등록하거나
     * 이미 등록된 경우 해당 개발자의 ID를 반환합니다.
     * 토큰이 유효하지 않으면 {@link UnauthorizedException}을 발생시킵니다.
     *
     * @param request 개발자 등록 요청 정보를 담은 객체. 액세스 토큰을 포함합니다.
     * @return 등록되거나 조회된 개발자의 ID.
     */
    @Transactional
    public Long registerDeveloper(final AuthRegisterRequest request) {
        String accessToken = request.getAccessToken();

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        String userName = jwtTokenProvider.getUserName(accessToken);

        return apiUserRepository.findByUserName(userName)
                .map(ApiUser::getDeveloperId)
                .orElseGet(() -> {
                    ApiUser newUser = ApiUser.builder()
                            .userName(userName)
                            .userType("개발자")
                            .build();
                    ApiUser saved = apiUserRepository.save(newUser);
                    return saved.getDeveloperId();
                });
    }
}
