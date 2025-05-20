package com.AnalysisAPIserver.domain.auth.service;

import com.AnalysisAPIserver.domain.auth.dto.AuthDeveloperInfoResponse;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterRequest;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.jwt.JwtTokenProvider;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
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
     * 개발자를 등록합니다. JWT 토큰에서 userName을 추출하여 저장하고,
     * 서버에서 새로운 developerId (Long)를 생성하여 반환합니다.

     *
     * @param request 등록 요청 DTO (accessToken 포함)
     * @return 생성되거나 기존에 있던 사용자의 developerId (Long)
     * @throws UnauthorizedException 토큰이 유효하지 않거나 userName 없는 경우
     */
    @Transactional
    public Long registerDeveloper(final AuthRegisterRequest request) {
        String accessToken = request.getAccessToken();
        if (!jwtTokenProvider.validateToken(accessToken)) {

            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        String userName = jwtTokenProvider.getUserName(accessToken);
        if (userName == null || userName.trim().isEmpty()) {

            throw new UnauthorizedException(
                    "JWT 토큰에서 사용자 이름을 추출할 수 없습니다.");
        }

        Optional<ApiUser> existingUserOptional =
                apiUserRepository.findByUserName(userName);
        if (existingUserOptional.isPresent()) {

            return existingUserOptional.get().getDeveloperId();
        }

        ApiUser newApiUser = ApiUser.builder()
                .userName(userName)
                .userType("개발자")
                .build();

        ApiUser savedUser = apiUserRepository.save(newApiUser);

        return savedUser.getDeveloperId();
    }


    /**
     * 주어진 액세스 토큰을 사용하여 개발자가 존재하지 않으면 새로 등록.
     * 토큰이 유효하지 않으면 {@link IllegalArgumentException}을 발생.
     *
     * @param accessToken 검증 및 사용자 정보 추출에 사용될 JWT 액세스 토큰.
     */
    @Transactional
    public void registerIfNotExists(final String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("Invalid JWT token.");
        }

        Long developerIdFromToken = jwtTokenProvider
                .getDeveloperId(accessToken);
        if (developerIdFromToken == null) {

            throw new IllegalArgumentException("JWT token does not contain"
                    + " developerId claim for registerIfNotExists.");
        }

        if (!apiUserRepository.existsById(developerIdFromToken)) {
            String userNameFromTokenForFilter =
                    jwtTokenProvider.getUserName(accessToken);

            if (userNameFromTokenForFilter == null
                    || userNameFromTokenForFilter.trim().isEmpty()) {

                throw new IllegalArgumentException("JWT token does not contain"
                        + " userName claim for registerIfNotExists.");
            }

            ApiUser apiUser = ApiUser.builder()
                    .developerId(developerIdFromToken)
                    .userName(userNameFromTokenForFilter)
                    .userType("개발자")
                    .createdAt(LocalDateTime.now())
                    .build();
            apiUserRepository.save(apiUser);
        }
    }

    /**
     * 주어진 액세스 토큰을 사용하여 개발자 정보를 조회합니다.
     * 토큰이 유효하지 않거나 등록되지 않은 개발자인 경우 {@link UnauthorizedException}을 발생.
     *
     * @param accessToken 개발자 정보 조회에 사용될 JWT 액세스 토큰.
     * @return 조회된 개발자 정보를 담은 {@link AuthDeveloperInfoResponse} 객체.
     */
    @Transactional(readOnly = true)
    public AuthDeveloperInfoResponse getDeveloperInfo(
            final String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        Long developerId = jwtTokenProvider.getDeveloperId(accessToken);

        if (developerId == null) {
            throw new UnauthorizedException(
                    "토큰에서 developerId를 추출할 수 없습니다.");
        }
        ApiUser user = apiUserRepository.findById(developerId)
                .orElseThrow(()
                        -> new UnauthorizedException("등록되지 않은 개발자입니다."));

        AuthDeveloperInfoResponse.User userDto =
                new AuthDeveloperInfoResponse.User(
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
                                final Long developerId) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        Long tokenDeveloperId = jwtTokenProvider.getDeveloperId(accessToken);

        if (tokenDeveloperId == null) {
            throw new UnauthorizedException(
                    "토큰에서 developerId를 추출할 수 없어 권한 확인이 불가합니다.");
        }
        if (!tokenDeveloperId.equals(developerId)) {
            throw new UnauthorizedException("본인의 계정만 삭제할 수 있습니다.");
        }

        ApiUser user = apiUserRepository.findById(developerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 개발자입니다."));

        apiUserRepository.delete(user);
    }

    /**
     * 주어진 액세스 토큰의 유효성을 확인하고, 해당 토큰과 연결된 사용자가
     * 데이터베이스에 존재하는지 확인하여 순수한 로그인 상태를 반환합니다.
     *
     * @param accessToken 검증할 JWT 액세스 토큰.
     * @return 토큰이 유효하고 해당 사용자가 DB에 존재하면 true,
     * 토큰은 유효하나 해당 사용자가 DB에 없으면 false.
     * @throws UnauthorizedException 토큰 자체가 유효하지 않거나,
     * 토큰에서 사용자 식별 정보(예: developerId)를 추출할 수 없는 경우.
     */
    @Transactional(readOnly = true)
    public boolean verifyLoginStatus(final String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException(
                    "유효하지 않은 토큰입니다 (기본 검증 실패).");
        }

        Long developerIdFromToken;
        try {
            developerIdFromToken = jwtTokenProvider.getDeveloperId(accessToken);
            if (developerIdFromToken == null) {
                throw new UnauthorizedException(
                        "토큰에서 developerId 클레임을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            String errorMessage = "토큰에서 사용자 식별 정보 추출 실패: ";
            throw new UnauthorizedException(errorMessage + e.getMessage());
        }

        return apiUserRepository.existsById(developerIdFromToken);
    }

}
