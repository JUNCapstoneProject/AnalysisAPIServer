package com.AnalysisAPIserver.common.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰을 생성하고 검증하는 유틸 클래스이다.
 */
@Component
public final class JwtUtil {

    /**
     * JWT 서명용 비밀키.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 토큰 만료 시간 (24시간).
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

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
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    /**
     * 토큰에서 이메일을 추출한다.
     *
     * @param token JWT 토큰
     * @return 이메일
     */
    public String getEmailFromToken(final String token) {
        return Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰 유효성을 검증한다.
     *
     * @param token JWT 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
