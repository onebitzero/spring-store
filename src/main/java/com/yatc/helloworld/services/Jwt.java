package com.yatc.helloworld.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final Claims claims;

    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public String getRole() {
        return claims.get("role").toString();
    }

    public String toString() {
        return Jwts
                .builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }
}
