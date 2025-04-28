package com.AnalysisAPIserver.domain.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 인증 필터이다.
 */
@Component
@RequiredArgsConstructor
public final class JwtFilter extends OncePerRequestFilter {

    /**
     * JWT 유틸리티.
     */
    private final JwtProvider jwtProvider;

    /**
     * Bearer 접두사 길이.
     */
    private static final int TOKEN_PREFIX_LENGTH = 7;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && this.jwtProvider.validateToken(token)) {
            String email = this.jwtProvider.getEmailFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, null);
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 토큰을 추출한다.
     *
     * @param request 요청 객체
     * @return 추출된 토큰 문자열
     */
    private String resolveToken(final HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer "))
                ? bearer.substring(TOKEN_PREFIX_LENGTH)
                : null;
    }
}
