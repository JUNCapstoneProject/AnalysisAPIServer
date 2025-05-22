package com.AnalysisAPIserver.domain.auth.service;


import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
import com.AnalysisAPIserver.domain.auth.dto.AuthDeveloperInfoResponse;
import com.AnalysisAPIserver.domain.auth.dto.AuthRegisterRequest;
import com.AnalysisAPIserver.domain.auth.exception.ResourceNotFoundException;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.jwt.JwtTokenProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 이 클래스는 상속을 허용하지 않습니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService { // DesignForExtension 해결을 위해 final 추가

    /**
     * 로깅을 위한 Logger 객체입니다.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(AuthService.class);
    /**
     * API 사용자 정보에 접근하기 위한 Repository입니다.
     */
    private final ApiUserRepository apiUserRepository;
    /**
     * JWT 토큰 생성 및 검증을 담당하는 Provider입니다.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT의 bearer.
     */
    private static final String BEARER_PREFIX = "Bearer ";
    /**
     * JWT의 토큰 길이.
     */
    private static final int TOKEN_SUMMARY_LENGTH = 10;


    /**
     * "Bearer " 접두사를 제거하고 순수 토큰 문자열을 추출합니다.
     *
     * @param bearerToken "Bearer " 접두사를 포함할 수 있는 토큰 문자열.
     * @return 순수 토큰 문자열 또는 유효하지 않은 경우 null.
     */
    private String extractPureToken(final String bearerToken) {
        if (!StringUtils.hasText(bearerToken)) {
            return null;
        }
        if (bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        LOGGER.trace("Token string did not start with '{}', returning as is "
                        + "for validation: {}", BEARER_PREFIX,
                summarizeTokenForLog(bearerToken));
        return bearerToken;
    }

    /**
     * 로깅을 위해 토큰 문자열을 요약합니다.
     *
     * @param token 요약할 토큰 문자열.
     * @return 요약된 토큰 문자열 (기본 10자).
     */
    private String summarizeTokenForLog(final String token) {
        if (token == null) { // NeedBraces 해결
            return "null";
        }
        return token.length() > TOKEN_SUMMARY_LENGTH
                ? token.substring(0, TOKEN_SUMMARY_LENGTH) + "..."
                : token;
    }

    /**
     * 새로운 개발자를 등록합니다.
     * 제공된 액세스 토큰을 검증하고, 토큰의 사용자 이름으로 기존 사용자를 확인합니다.
     * 존재하지 않으면 새로운 사용자를 생성하고 저장합니다.
     *
     * @param request 개발자 등록 요청 정보 (액세스 토큰 포함).
     * @return 생성되거나 기존에 존재하던 개발자의 ID.
     * @throws UnauthorizedException 토큰이 유효하지 않거나 사용자 이름을 추출할 수 없는 경우.
     */
    @Transactional
    public Long registerDeveloper(final AuthRegisterRequest request) {
        String bearerTokenInRequest = request.getAccessToken();
        String pureAccessToken = extractPureToken(bearerTokenInRequest);

        if (pureAccessToken == null || pureAccessToken.trim().isEmpty()) {
            throw new UnauthorizedException("제공된 액세스 토큰이 비어있거나 "
                    + "유효하지 않습니다 (registerDeveloper).");
        }
        if (!jwtTokenProvider.validateToken(pureAccessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다 "
                    + "(registerDeveloper).");
        }
        String userNameFromToken
                = jwtTokenProvider.getUserName(pureAccessToken);
        if (userNameFromToken == null || userNameFromToken.trim().isEmpty()) {
            throw new UnauthorizedException("JWT 토큰에서 사용자 이름을 추출할 수 "
                    + "없습니다 (registerDeveloper).");
        }
        Optional<ApiUser> existingUserOptional =
                apiUserRepository.findByUserName(userNameFromToken);
        if (existingUserOptional.isPresent()) {
            LOGGER.info("User with userName '{}' already exists. "
                            + "Returning existing developerId: {}",
                    userNameFromToken,
                    existingUserOptional.get().getDeveloperId());
            return existingUserOptional.get().getDeveloperId();
        }
        ApiUser newApiUser = ApiUser.builder()
                .userName(userNameFromToken)
                .userType("개발자")
                .build();
        ApiUser savedUser = apiUserRepository.save(newApiUser);
        LOGGER.info("New user registered with userName '{}'. "
                        + "Assigned developerId: {}",
                userNameFromToken, savedUser.getDeveloperId());
        return savedUser.getDeveloperId();
    }

    /**
     * 필터 등에서 호출되어, 토큰 기반으로 사용자가 존재하지 않으면 등록합니다.
     * 주로 내부 시스템 호출용으로 사용됩니다.
     *
     * @param pureAccessTokenFromFilter 순수 액세스 토큰 문자열.
     * @throws IllegalArgumentException 토큰이 유효하지 않거나 사용자 이름을 포함하지 않는 경우.
     */
    @Transactional
    public void registerIfNotExists(final String pureAccessTokenFromFilter) {
        if (pureAccessTokenFromFilter == null
                || pureAccessTokenFromFilter.trim().isEmpty()) {
            throw
                    new IllegalArgumentException("Access token"
                            + " cannot be null or empty "
                    + "(registerIfNotExists).");
        }
        if (!jwtTokenProvider.validateToken(pureAccessTokenFromFilter)) {
            throw new IllegalArgumentException("Invalid JWT token "
                    + "(registerIfNotExists).");
        }
        String userNameFromToken =
                jwtTokenProvider.getUserName(pureAccessTokenFromFilter);
        if (userNameFromToken == null || userNameFromToken.trim().isEmpty()) {
            throw
                    new IllegalArgumentException("JWT token does not"
                            + " contain userName "
                    + "claim (registerIfNotExists).");
        }
        if (!apiUserRepository.findByUserName(userNameFromToken).isPresent()) {
            LOGGER.info("User with userName '{}' not found."
                    + " Registering new user "
                    + "(called from registerIfNotExists).", userNameFromToken);
            ApiUser newUser = ApiUser.builder()
                    .userName(userNameFromToken)
                    .userType("개발자")
                    .build();
            apiUserRepository.save(newUser);
        }
    }

    /**
     * 특정 개발자 ID의 정보를 조회합니다. 토큰을 통해 요청자 본인인지 확인합니다.
     *
     * @param bearerToken         인증 토큰 ("Bearer " 접두사 포함).
     * @param pathDeveloperId     조회할 개발자의 ID (URL 경로에서 받음).
     * @return 조회된 개발자 정보를 담은 {@link AuthDeveloperInfoResponse} 객체.
     * @throws UnauthorizedException 토큰이 유효하지 않거나, 토큰에서 userName을 추출할 수 없거나,
     * 조회 권한이 없는 경우(본인 정보가 아님).
     * @throws ResourceNotFoundException 해당 pathDeveloperId의 사용자를 찾을 수 없는 경우.
     */
    @Transactional(readOnly = true)
    public AuthDeveloperInfoResponse
    getDeveloperInfo(final String bearerToken,
                     final Long pathDeveloperId) {
        String accessToken = extractPureToken(bearerToken);
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new UnauthorizedException("액세스 토큰이 제공되지 않았습니다 "
                    + "(getDeveloperInfo).");
        }
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다 "
                    + "(getDeveloperInfo).");
        }

        String userNameFromToken = jwtTokenProvider.getUserName(accessToken);
        if (userNameFromToken == null || userNameFromToken.trim().isEmpty()) {
            throw new UnauthorizedException("토큰에서 사용자 이름을 추출할 수 없습니다 "
                    + "(getDeveloperInfo).");
        }

        ApiUser requester = apiUserRepository.findByUserName(userNameFromToken)
                .orElseThrow(() -> {
                    LOGGER.warn("No user (requester) found "
                            + "with userName '{}' from "
                            + "token (getDeveloperInfo).", userNameFromToken);
                    return new UnauthorizedException("요청한 토큰에 해당하는 사용자를 "
                            + "찾을 수 없습니다.");
                });

        if (!requester.getDeveloperId().equals(pathDeveloperId)) {
            LOGGER.warn("Unauthorized attempt to access developer info. "
                            + "Requester ID: {}, Target ID: {}",
                    requester.getDeveloperId(), pathDeveloperId);
            throw new UnauthorizedException("자신의 정보만 조회할 수 있습니다.");
        }

        ApiUser userToDisplay = apiUserRepository.findById(pathDeveloperId)
                .orElseThrow(() -> {
                    LOGGER.error("Data inconsistency: User with ID {}"
                                    + " (verified by "
                            + "token) not found in DB for display.",
                            pathDeveloperId);
                    return new ResourceNotFoundException("조회하려는 개발자 정보를 찾을 수 "
                            + "없습니다. (ID: " + pathDeveloperId + ")");
                });

        AuthDeveloperInfoResponse.User userDto =
                new AuthDeveloperInfoResponse.User(
                        userToDisplay.getUserName(),
                        userToDisplay.getUserType(),
                        userToDisplay.getCreatedAt() != null
                                ? userToDisplay.getCreatedAt()
                                .toLocalDate() : null,
                        userToDisplay.getDeveloperId()
                );
        return AuthDeveloperInfoResponse.builder().user(userDto).build();
    }

    /**
     * 개발자 계정을 삭제합니다.
     * 토큰을 통해 본인 계정인지 확인 후 삭제를 진행합니다.
     *
     * @param bearerToken         인증 토큰 ("Bearer " 접두사 포함).
     * @param developerIdToDelete 삭제할 개발자 계정의 ID.
     * @throws UnauthorizedException 토큰이 유효하지 않거나, 본인 계정이 아닌 경우.
     * @throws ResourceNotFoundException 삭제할 계정을 찾을 수 없는 경우.
     */
    @Transactional
    public void deleteDeveloper(final String bearerToken,
                                final Long developerIdToDelete) {
        String accessToken = extractPureToken(bearerToken);
        if (accessToken == null
                || !jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다 "
                    + "(deleteDeveloper).");
        }
        String userNameFromToken = jwtTokenProvider.getUserName(accessToken);
        if (userNameFromToken == null || userNameFromToken.trim().isEmpty()) {
            throw new UnauthorizedException("토큰에서 사용자 이름을 추출할 수 없어 "
                    + "권한을 확인할 수 없습니다 (deleteDeveloper).");
        }
        ApiUser userFromToken
                = apiUserRepository.findByUserName(userNameFromToken)
                .orElseThrow(()
                        -> new UnauthorizedException("요청한 토큰에 해당하는 "
                        + "사용자를 찾을 수 없습니다."));

        if (!userFromToken.getDeveloperId().equals(developerIdToDelete)) {
            throw new UnauthorizedException("본인의 계정만 삭제할 수 있습니다.");
        }
        if (!apiUserRepository.existsById(developerIdToDelete)) {
            throw new ResourceNotFoundException("삭제할 개발자 계정(ID: "
                    + developerIdToDelete + ")을 찾을 수 없습니다.");
        }
        apiUserRepository.deleteById(developerIdToDelete);
        LOGGER.info("Successfully deleted developer account with id: {} "
                        + "(requested by userName: '{}')", developerIdToDelete,
                userNameFromToken);
    }

    /**
     * 제공된 토큰을 기반으로 사용자의 로그인 상태(존재 유무)를 검증합니다.
     *
     * @param bearerToken 인증 토큰 ("Bearer " 접두사 포함).
     * @return 사용자가 존재하고 토큰이 유효하면 true, 그렇지 않으면 false 또는 예외 발생.
     * @throws UnauthorizedException 토큰이 유효하지 않거나 사용자 이름을 포함하지 않는 경우.
     */
    @Transactional(readOnly = true)
    public boolean verifyLoginStatus(final String bearerToken) {
        String accessToken = extractPureToken(bearerToken);
        if (accessToken == null
                || !jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다 (기본 검증 실패 "
                    + "- verifyLoginStatus).");
        }
        String userName = jwtTokenProvider.getUserName(accessToken);
        if (userName == null || userName.trim().isEmpty()) {
            throw new UnauthorizedException("토큰에서 userName 클레임을 찾을 수 없습니다 "
                    + "(verifyLoginStatus).");
        }
        return apiUserRepository.findByUserName(userName).isPresent();
    }
}
