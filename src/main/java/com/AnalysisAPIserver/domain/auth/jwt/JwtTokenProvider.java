package com.AnalysisAPIserver.domain.auth.jwt;


import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;


/**
 * JWT 토큰 생성, 검증 및 클레임 추출을 담당하는 클래스입니다.
 */
@Component
public final class JwtTokenProvider { // 클래스는 final로 선언

    /**
     * JWT 서명에 사용될 비밀 키 문자열입니다.
     * application.yml 또는 application.properties 파일에서 jwt.secret 값으로 주입받습니다.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * HMAC SHA 알고리즘을 사용하여 생성된 JWT 서명 키입니다.
     */
    private Key key;

    /**
     * 빈 초기화 시 호출되어 JWT 서명 키를 생성합니다.
     * secret 문자열을 바이트 배열로 변환하여 HMAC SHA 키를 생성합니다.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 토큰 문자열.
     * @return 토큰이 유효하면 true, 그렇지 않으면 false.
     */
    public boolean validateToken(final String token) { // FinalParameters 적용
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token); // LineLength 해결
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 주어진 JWT 토큰에서 클레임(claims)을 추출합니다.
     *
     * @param token 클레임을 추출할 JWT 토큰 문자열.
     * @return 추출된 클레임 객체.
     */
    public Claims getClaims(final String token) { // FinalParameters 적용
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 주어진 JWT 토큰에서 개발자 ID를 추출합니다.
     *
     * @param token 개발자 ID를 추출할 JWT 토큰 문자열.
     * @return 추출된 개발자 ID (Long 타입).
     */
    public Long getDeveloperId(final String token) { // FinalParameters 적용
        return getClaims(token).get("developerId", Long.class);
    }

    /**
     * 주어진 JWT 토큰에서 사용자 이름을 추출합니다.
     *
     * @param token 사용자 이름을 추출할 JWT 토큰 문자열.
     * @return 추출된 사용자 이름 (String 타입).
     */
    public String getUserName(final String token) { // FinalParameters 적용
        return getClaims(token).get("userName", String.class);
    }
}
