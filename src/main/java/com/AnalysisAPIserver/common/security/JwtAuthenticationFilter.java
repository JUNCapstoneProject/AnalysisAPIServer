package com.AnalysisAPIserver.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 인증 필터이다.
 */
@Component
public final class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT 유틸리티.
     */
    private final JwtUtil jwtUtil;

    /**
     * 사용자 인증 서비스.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * JwtAuthenticationFilter 생성자.
     *
     * @param jwtUtilParam JWT 유틸리티
     * @param userDetailsServiceParam 사용자 인증 서비스
     */
    public JwtAuthenticationFilter(
            final JwtUtil jwtUtilParam,
            final CustomUserDetailsService userDetailsServiceParam) {
        this.jwtUtil = jwtUtilParam;
        this.userDetailsService = userDetailsServiceParam;
    }

    /**
     * 요청을 필터링하여 JWT 인증을 수행한다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final int tokenPrefixLength = 7;
            token = authHeader.substring(tokenPrefixLength);
            email = this.jwtUtil.getEmailFromToken(token);
        }

        if (email != null
                && SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            UserDetails userDetails =
                    this.userDetailsService.loadUserByUsername(email);

            if (this.jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
