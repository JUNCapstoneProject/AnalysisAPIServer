package com.AnalysisAPIserver.domain.auth.jwt;

import com.AnalysisAPIserver.domain.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 인증 필터입니다.
 * {@link OncePerRequestFilter}를 상속받아 요청당 한 번만 실행되도록 합니다.
 */
@Component
@RequiredArgsConstructor
public final class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT 토큰을 생성하고 검증하는 프로바이더입니다.
     */
    private final JwtTokenProvider jwtTokenProvider;
    /**
     * 인증 관련 비즈니스 로직을 처리하는 서비스입니다.
     */
    private final AuthService authService;

    /**
     * "Bearer " 접두사의 길이.
     */
    private static final int BEARER_TOKEN_PREFIX_LENGTH = 7;

    /**
     * 실제 필터링 로직을 수행합니다.
     * HTTP 요청 헤더에서 Authorization 토큰을 추출하여 JWT 유효성 검사를 수행하고,
     * 유효한 경우 해당 토큰을 사용하여 사용자를 인증합니다.
     *
     * @param request     HTTP 요청 객체.
     * @param response    HTTP 응답 객체.
     * @param filterChain 필터 체인 객체.
     * @throws ServletException 서블릿 관련 예외 발생 시.
     * @throws IOException      입출력 예외 발생 시.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String accessToken
                    = bearerToken.substring(BEARER_TOKEN_PREFIX_LENGTH);
            authService.registerIfNotExists(
                    accessToken);
        }

        filterChain.doFilter(request, response);
    }
}
