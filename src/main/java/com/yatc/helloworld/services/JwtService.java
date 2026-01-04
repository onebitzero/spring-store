package com.yatc.helloworld.services;

import com.yatc.helloworld.config.JwtConfig;
import com.yatc.helloworld.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    private Jwt generateToken(User user, int tokenExpiration) {
        Claims claims = Jwts
                .claims()
                .subject(user.getId().toString())
                .add(Map.of(
                        "email",
                        user.getEmail(),
                        "name",
                        user.getName(),
                        "role",
                        user.getRole().toString()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiry());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiry());
    }

    public Jwt parseToken(String token) {
        try {
            Claims claims = getClaims(token);

            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
