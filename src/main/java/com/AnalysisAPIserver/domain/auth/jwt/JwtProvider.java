package com.AnalysisAPIserver.domain.auth.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * JWT 생성 및 검증 유틸 클래스이다.
 */
@Component
public final class JwtProvider {

    /**
     * JWT 서명 키.
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 토큰 만료 시간 (24시간).
     */
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    /**
     * 토큰을 생성한다.
     *
     * @param email 사용자 이메일
     * @return 생성된 토큰
     */
    public String generateToken(final String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()
                        + EXPIRATION))
                .signWith(this.key)
                .compact();
    }

    /**
     * 토큰에서 이메일을 추출한다.
     *
     * @param token 토큰
     * @return 이메일
     */
    public String getEmailFromToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰의 유효성을 검증한다.
     *
     * @param token 토큰
     * @return 유효 여부
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
