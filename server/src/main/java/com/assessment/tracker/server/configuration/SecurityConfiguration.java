package com.assessment.tracker.server.configuration;

import com.assessment.tracker.server.persistence.entities.User;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    /*
     * This is to be rewritten default implementation is to permit any request we
     * will have
     * Roles etc. therefore we need to use spring security roles
     */

    private final JpaUserDetailsService userDetailsService;
    private final RsaKeyProperties rsaKeys;

    public SecurityConfiguration(JpaUserDetailsService userDetailsService, RsaKeyProperties rsaKeys) {
        this.userDetailsService = userDetailsService;
        this.rsaKeys = rsaKeys;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // allow H2 console (optional)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // disable CSRF because you are using JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/signup").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // sgnup is only like this for testing should be only permitted for TST
                        // -> TODO: account creation only done by TST
                        .anyRequest().authenticated())

                // JWT validation (your RSA public key via Nimbus)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))

                // do not use session (JWT = stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.getRsaPublicKey()).build();
    }

    @Bean
    JwtEncoder encoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.getRsaPublicKey()).privateKey(rsaKeys.getRsaPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
