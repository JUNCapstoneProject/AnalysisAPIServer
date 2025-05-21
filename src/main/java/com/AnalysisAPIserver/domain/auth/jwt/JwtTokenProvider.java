package com.AnalysisAPIserver.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders; // Base64 디코더 사용
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.WeakKeyException;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * JWT 토큰 생성, 검증 및 클레임 추출을 담당하는 클래스입니다.
 * HS384 알고리즘 및 Base64 인코딩된 Secret Key 사용을 가정합니다.
 */
@Component
public final class JwtTokenProvider {

    /**
     * 로깅을 위한 Logger 객체입니다.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * 16진수 변환 시 사용되는 기수(radix)입니다.
     */
    private static final int HEX_RADIX = 16;
    /**
     * 16진수 니블(nibble) 변환 시 사용되는 비트 시프트 값입니다.
     */
    private static final int HEX_NIBBLE_SHIFT = 4;
    /**
     * 1바이트(byte)를 구성하는 비트(bit) 수입니다.
     */
    private static final int BITS_PER_BYTE = 8;
    /**
     * 토큰 요약 시 기본 최대 길이입니다.
     */
    private static final int DEFAULT_SUMMARY_MAX_LENGTH = 10;

    /**
     * .env 에서 읽어올 Base64 인코딩된 시크릿 키.
     */
    @Value("${jwt.secret}")
    private String base64Secret;

    /**
     * JWT 서명에 사용될 키 객체.
     */
    private Key key;
    /**
     * JWT 서명에 사용될 알고리즘.
     */
    private final SignatureAlgorithm signatureAlgorithm
            = SignatureAlgorithm.HS384;

    /**
     * 16진수 문자열을 byte 배열로 변환합니다.
     *
     * @param hex 변환할 16진수 문자열.
     * @return 변환된 byte 배열.
     */
    private byte[] hexStringToByteArray(final String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), HEX_RADIX)
                    << HEX_NIBBLE_SHIFT)
                    + Character.digit(hex.charAt(i + 1), HEX_RADIX));
        }
        return data;
    }

    /**
     * JWT 키를 초기화합니다.
     * Base64로 인코딩된 시크릿 키를 디코딩하여 사용합니다.
     * 키 길이가 알고리즘에서 요구하는 최소 길이보다 짧으면 예외를 발생시킵니다.
     *
     * @throws IllegalArgumentException JWT 시크릿 키가 부적절하게 설정된 경우.
     * @throws WeakKeyException       디코딩된 키가 알고리즘에 비해 너무 약한 경우.
     * @throws IllegalStateException  키 초기화 중 예상치 못한 오류 발생 시.
     */
    @PostConstruct
    public void init() {
        LOGGER.info("Initializing JWT Key for {} using Base64-decoded secret "
                + "from property.", signatureAlgorithm.getValue());
        if (base64Secret == null || base64Secret.trim().isEmpty()) {
            LOGGER.error("JWT secret key (base64Secret from property) is not "
                    + "configured properly. It is null or empty.");
            throw new IllegalArgumentException("JWT secret key (base64Secret) "
                    + "cannot be null or empty. Check .env or "
                    + "application.properties.");
        }
        try {
            byte[] secretKeyBytes = hexStringToByteArray(base64Secret);
            LOGGER.info("Successfully decoded Base64 secret."
                            + " Byte array length: "
                            + "{} bytes ({} bits)",
                    secretKeyBytes.length,
                    secretKeyBytes.length * BITS_PER_BYTE);

            if (secretKeyBytes.length * BITS_PER_BYTE
                    < signatureAlgorithm.getMinKeyLength()) {
                String errorMessage = String.format(
                        "Configured JWT secret key"
                                + " (after Base64 decoding: %d bits) "
                                + "is SHORTER than"
                                + " the minimum required key length "
                                + "(%d bits) for the %s algorithm.",
                        secretKeyBytes.length * BITS_PER_BYTE,
                        signatureAlgorithm.getMinKeyLength(),
                        signatureAlgorithm.getValue()
                );
                LOGGER.error("--- CRITICAL KEY LENGTH ERROR ---");
                LOGGER.error(errorMessage);
                LOGGER.error("--- CRITICAL KEY LENGTH ERROR ---");
                throw new WeakKeyException(errorMessage);
            }

            this.key = Keys.hmacShaKeyFor(secretKeyBytes);
            LOGGER.info("JWT Key initialized. Intended Algorithm: {},"
                            + " Actual Key "
                            + "Algorithm (from key object): {}",
                    signatureAlgorithm.getValue(), this.key.getAlgorithm());

            if (!this.key.getAlgorithm().equals("HmacSHA384")) {
                LOGGER.warn("Potential algorithm mismatch."
                                + " Expected HS384, Got: {}. "
                                + "Key length: {} bytes. Secret (first {}): {}",
                        this.key.getAlgorithm(), secretKeyBytes.length,
                        summarizeToken(base64Secret));
            }

        } catch (IllegalArgumentException e) {
            LOGGER.error("Error decoding Base64 secret or invalid argument. "
                            + "Secret (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH, summarizeToken(base64Secret),
                    e.getMessage(), e);
            throw new IllegalStateException("Failed to initialize JWT Key"
                    + " due to "
                    + "invalid Base64 secret or argument", e);
        } catch (WeakKeyException wke) {
            LOGGER.error("--- WEAK KEY EXCEPTION ---");
            LOGGER.error("Provided key is not strong enough for {}. "
                            + "Decoded size: {} bits,"
                            + " Required: {} bits. Error: {}",
                    signatureAlgorithm.getValue(),
                    (base64Secret != null && !base64Secret.isBlank()
                            ? (Decoders.BASE64.decode(base64Secret).length
                            * BITS_PER_BYTE)
                            : "N/A"),
                    signatureAlgorithm.getMinKeyLength(),
                    wke.getMessage(), wke);
            LOGGER.error("--- WEAK KEY EXCEPTION ---");
            throw new IllegalStateException("Failed to initialize JWT Key"
                    + " due to "
                    + "weak key for " + signatureAlgorithm.getValue(), wke);
        } catch (Exception e) {
            LOGGER.error("Unexpected error initializing JWT Key. "
                            + "Secret (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH, summarizeToken(base64Secret),
                    e.getMessage(), e);
            throw new IllegalStateException("Failed to initialize JWT Key from "
                    + "decoded secret bytes", e);
        }
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검사합니다.
     *
     * @param token 검사할 JWT 토큰 문자열.
     * @return 토큰이 유효하면 true, 그렇지 않으면 false.
     */
    public boolean validateToken(final String token) {
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warn("Validation failed: Token string is null or empty.");
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            LOGGER.info("Token validation successful (first {} chars): {}",
                    DEFAULT_SUMMARY_MAX_LENGTH, summarizeToken(token));
            return true;
        } catch (WeakKeyException wke) {
            LOGGER.warn("Validation failed: Weak key. Error: {}",
                    wke.getMessage());
        } catch (SecurityException | MalformedJwtException e) {
            LOGGER.warn("Validation failed: Invalid signature/malformed. "
                            + "Token (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH,
                    summarizeToken(token), e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.warn("Validation failed: Expired token. "
                            + "Token (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH,
                    summarizeToken(token), e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.warn("Validation failed: Unsupported token. "
                            + "Token (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH,
                    summarizeToken(token), e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Validation failed: Invalid claims/token. "
                            + "Token (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH,
                    summarizeToken(token), e.getMessage());
        } catch (JwtException e) {
            LOGGER.warn("Validation failed: General JWT exception. "
                            + "Token (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH,
                    summarizeToken(token), e.getMessage());
        }
        return false;
    }

    /**
     * 주어진 JWT 토큰에서 클레임(Claims)을 추출합니다.
     *
     * @param token 클레임을 추출할 JWT 토큰 문자열.
     * @return 추출된 클레임 객체.
     * @throws JwtException 토큰 파싱 또는 검증 실패 시.
     */
    public Claims getClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 주어진 JWT 토큰에서 사용자 이름 (토큰 클레임: "username")을 추출합니다.
     *
     * @param token 사용자 이름을 추출할 JWT 토큰 문자열.
     * @return 추출된 사용자 이름 (String 타입).
     * @throws JwtException 토큰 파싱/검증 실패 시 또는 클레임 관련 문제 발생 시.
     */
    public String getUserName(final String token) {
        try {
            // 토큰에서 "username" (소문자) 클레임을 읽도록 수정
            return getClaims(token).get("username", String.class);
        } catch (Exception e) {
            LOGGER.warn("Failed to get 'username' claim. "
                            + "Token (first {}): [{}], Error: {}",
                    DEFAULT_SUMMARY_MAX_LENGTH,
                    summarizeToken(token), e.getMessage());
            throw new JwtException("Failed to extract 'username' claim: "
                    + e.getMessage(), e);
        }
    }

    /**
     * 로깅을 위해 토큰 또는 문자열을 요약합니다. (기본 처음 10자)
     *
     * @param token 요약할 문자열.
     * @return 요약된 문자열.
     */
    private String summarizeToken(final String token) {
        if (token == null) {
            return "null";
        }
        return token.length() > DEFAULT_SUMMARY_MAX_LENGTH
                ? token.substring(0, DEFAULT_SUMMARY_MAX_LENGTH) + "..."
                : token;
    }
}
