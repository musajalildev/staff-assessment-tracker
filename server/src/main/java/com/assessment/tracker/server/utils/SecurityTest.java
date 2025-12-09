package com.assessment.tracker.server.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class SecurityTest {
    /*
     * @Bean
     * public JwtAuthenticationConverter jwtAuthenticationConverter() {
     * JwtGrantedAuthoritiesConverter authoritiesConverter = new
     * JwtGrantedAuthoritiesConverter();
     * authoritiesConverter.setAuthorityPrefix("ROLE_"); // if you use ROLE_ prefix
     * 
     * JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
     * converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
     * return converter;
     * }/*
     */
}
