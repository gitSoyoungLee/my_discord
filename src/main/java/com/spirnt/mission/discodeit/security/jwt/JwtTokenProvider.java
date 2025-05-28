package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.Role;
import com.spirnt.mission.discodeit.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    private final JwtProperties jwtProperties;
    private final Clock clock = Clock.systemUTC();

    // 액세스 토큰 발급
    public String generateAccessToken(UserDto userDto) {
        return generateToken(userDto,
            jwtProperties.getAccessToken().getValiditySeconds(),
            TokenType.ACCESS);
    }

    // 리프레시 토큰 발급
    public String generateRefreshToken(UserDto userDto) {
        return generateToken(userDto,
            jwtProperties.getRefreshToken().getValiditySeconds(),
            TokenType.REFRESH);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalStateException e) {
            return false;
        }
    }


    // 액세스, 리프레시 토큰 생성
    private String generateToken(UserDto userDto, long validitySeconds,
        TokenType tokenType) {
        Instant now = clock.instant();
        Instant expiration = now.plusSeconds(validitySeconds);

        return Jwts.builder()
            .header()
            .add("typ", "JWT")
            .and()
            .issuer(jwtProperties.getIssuer())
            .issuedAt(Date.from(now))
            .subject(userDto.getUsername())
            .expiration(Date.from(expiration))
            .claim("type", tokenType.name())
            .claim("userId", userDto.getId())
            .claim("role", userDto.getRole().name())
            .signWith(getSignKey(), SIG.HS256)
            .compact();
    }

    //설정된 비밀 키를 서명 키로 변환
    private SecretKey getSignKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰에서 클레임 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .clockSkewSeconds(60)   // 시간 오차 60초 허용
            .build()
            .parseSignedClaims(token) // 이때 서명, 만료시간 검증 + claim 추출
            .getPayload();
    }

    public Authentication getAuthentication(String token) {
        // 클레임에 포함된 role에서 SimpleGrantedAuthority 리스트 생성
        Claims claims = getClaims(token);
        Role role = Role.valueOf(claims.get("role", String.class));
        Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();
        if (role != null) {
            authorities = List.of(new SimpleGrantedAuthority(role.name()));
        }
        User principal = new User(claims.getSubject(), "", "");
        principal.updateRole(role);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
