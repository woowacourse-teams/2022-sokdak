package com.wooteco.sokdak.support.token;


import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenManager {

    private final Key signingKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                            @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
    }

    @Override
    public String createToken(AuthInfo authInfo) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim("id", authInfo.getId())
                .claim("role", authInfo.getRole())
                .claim("nickname", authInfo.getNickname())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String getPayload(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public AuthInfo getParsedClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long id = claims.get("id", Long.class);
        String role = claims.get("role", String.class);
        String nickname = claims.get("nickname", String.class);
        return new AuthInfo(id, role, nickname);
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

