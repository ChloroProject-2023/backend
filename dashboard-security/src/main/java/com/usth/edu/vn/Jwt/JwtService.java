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
                .expiresIn(300)
                .innerSign() // default Signature algorithm: RS256
                .encrypt(); // default Key encryption algorithm: RSA-OAEP-256
    }
}
