package com.usth.edu.vn.Jwt;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class JwtService  {

    public String generateJwt(String issuer, String subject, Set<String> roles) {
        return Jwt.issuer(issuer)
                .subject(subject)
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }
}
