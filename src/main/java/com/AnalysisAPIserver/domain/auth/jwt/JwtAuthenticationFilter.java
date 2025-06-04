package com.AnalysisAPIserver.domain.auth.jwt;

import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 인증 필터입니다.
 * {@link OncePerRequestFilter}를 상속받아 요청당 한 번만 실행되도록 합니다.
 */
@Component
@RequiredArgsConstructor
public final class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 로깅을 위한 Logger 객체입니다.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    /**
     * JWT 토큰 생성 및 검증을 담당하는 Provider입니다.
     */
    private final JwtTokenProvider jwtTokenProvider;
    /**
     * 인증 관련 비즈니스 로직을 처리하는 서비스입니다.
     */
    private final AuthService authService;

    /**
     * HTTP 요청 헤더에서 인증 정보를 나타내는 헤더 이름입니다.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    /**
     * Bearer 토큰 인증 스킴을 나타내는 접두사입니다.
     */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * JWT 인증 필터가 적용될 경로입니다.
     */
    private static final String TARGET_PATH_PREFIX = "/api/auth/";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        final String requestUri = request.getRequestURI();


        if (!requestUri.startsWith(TARGET_PATH_PREFIX)) {
            LOGGER.trace("JwtAuthenticationFilter:"
                    + " Skipping filter for URI: {}", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        LOGGER.debug("JwtAuthenticationFilter: "
                + "Processing request for URI: {}", requestUri);

        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String pureAccessToken = null;

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith(BEARER_PREFIX)) {
            pureAccessToken = bearerToken.substring(BEARER_PREFIX.length());
        }

        if (pureAccessToken != null) {
            try {
                // jwtTokenProvider.validateToken(pureAccessToken);
                authService.registerIfNotExists(pureAccessToken);
                LOGGER.debug("JwtAuthenticationFilter: Token processed by "
                                + "registerIfNotExists for request URI: {}",
                        requestUri);
            } catch (IllegalArgumentException | UnauthorizedException e) {
                LOGGER.warn("JwtAuthenticationFilter:"
                                + " Invalid token processed by "
                                + "registerIfNotExists for URI: {}. Error: {}",
                        requestUri, e.getMessage());

            }
        } else {
            LOGGER.trace("JwtAuthenticationFilter:"
                            + " No JWT 'Bearer' token found in "
                            + "request headers for URI: {}",
                    requestUri);

        }

        filterChain.doFilter(request, response);
    }
}
