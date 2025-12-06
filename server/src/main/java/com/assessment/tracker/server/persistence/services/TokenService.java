package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.dto.authenticationDTOs.TokenDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public TokenDTO generateToken(Collection<? extends GrantedAuthority> authorities, String username) {
        Instant now = Instant.now();
        String scope = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now) // current time can be used in logging
                .expiresAt(now.plus(2, ChronoUnit.HOURS)) // token expiry time
                .subject(username)
                .claim("scope", scope)
                .build();
        return new TokenDTO(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }
}
