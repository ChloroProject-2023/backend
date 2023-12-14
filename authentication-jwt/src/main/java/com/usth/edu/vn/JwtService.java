package com.usth.edu.vn;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class JwtService {

    public String generateJwt() {
        Set<String> roles = new HashSet<>(
                Arrays.asList("admin", "user")
        );
        return Jwt.issuer("user-management")
                .subject("user-management")
                .groups(roles)
                .expiresAt(
                        System.currentTimeMillis() + 3600
                )
                .sign();
    }
}
