package com.usth.edu.vn.Jwt;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;

@Singleton
public class JwtService  {

    public String generateJwt(String issuer, String subject, String role) {
        return Jwt.issuer(issuer)
                .subject(subject)
                .groups(role)
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }
}
