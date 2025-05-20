package com.AnalysisAPIserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 스프링 시큐리티 설정 클래스이다.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * SecurityFilterChain을 설정한다.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http)
            throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()
                        .anyRequest().permitAll() // 임시로 모두 허용
                )
                /*.addFilterBefore(
                        this.jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)*/
                .build();
    }

    /**
     * AuthenticationManager 빈을 등록한다.
     *
     * @param configuration AuthenticationConfiguration
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
