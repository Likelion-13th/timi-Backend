package likelion13th.shop.login.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component

public class TokenProvider {
    private final Key secretKey;
    private final long accessTokenExpiration; // accessToken 만료
    private final long refreshTokenExpiration; // refreshToken 만료

    public TokenProvider(
            @Value("${JWT_SECRET}") String secretKey, // 환경변수, 설정에서 secretKey
            @Value("${JWT_EXPIRATION}") long accessTokenExpiration,
            @Value("${JWT_REFRESH_EXPIRATION}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // access/refresh 한 번에 생성 (로그인/재발급할 때)
    public JwtDto generateTokens(UserDetails userDetails) {
        log.info("JWT 생성: 사용자 {}", userDetails.getUsername());

        String userId = userDetails.getUsername();

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // accessToken와 refreshToken에 권한 담음
        String accessToken = createToken(userId, authorities, accessTokenExpiration);
        String refreshToken = createToken(userId, null, refreshTokenExpiration);

        log.info("JWT 생성 완료: 사용자 {}", userDetails.getUsername());
        return new JwtDto(accessToken, refreshToken);

    }

    // JWT 문자열 생성
    private String createToken(String providerId, String authorities, long expirationTime) {
        JwtBuilder jwtbuilder = Jwts.builder()
                .setSubject(providerId) // 고유 식별자(어떤 사용자의 토큰인지!)
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis()+ expirationTime)) // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256);

        if(authorities != null) {
            jwtbuilder.claim("authorities", authorities);
        }

        return jwtbuilder.compact();
    }

    // 서명/만료 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 검증에서 사용할 키
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 유효한 토큰 파싱 -> Claims 반환
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch(ExpiredJwtException e) {
            log.warn("토큰 만료");
            throw e;
        } catch(JwtException e) {
            log.warn("JWT 파싱 실패");
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }
    }

    // Claims에서 권한 문자열을 꺼내 GrantedAuthority로 복원
    public Collection<? extends GrantedAuthority> getAuthFromClaims(Claims claims) {
        String authoritiesString = claims.get("authorities", String.class);
        if(authoritiesString == null || authoritiesString.isEmpty()) {
            log.warn("권한 정보가 없다 - 기본 ROLE_USER 부여");
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // 만료된 토큰이어도 클레임만 가져오고 싶을 때
    public Claims parseClaimsAllowExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody(); // 유효하면 그대로 반환
        }catch(ExpiredJwtException e) {
            return e.getClaims(); // 만료여도 클레임은 정리해줌
        }
    }

}

/*
1. 왜 필요한가?
- 어떤 사용자가 로그인을 요청했는지 기억하기 위한 서명된 토큰이 필요한데,
  이때 TokenProvider가 JWT를 생성하고 검증하고 파싱하는 역할을 위함.
- accessToken과 refreshToken을 만들어 사용자 인증 / 인가를 하고, 로그인 유지와 재발급 흐름을 구현함.

2. 없으면/틀리면?
- 토큰을 못 만들거나 잘못 검증하면, 로그인한 사용자를 확신할 수 없고, 에러가 난다.
- 서명 키와 토큰 만료 설정이 얽히게 되면 로그인과정과 보안에 문제가 생길 수 있다.
 */
