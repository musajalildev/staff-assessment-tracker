package com.assessment.tracker.server.api.dto.authenticationDTOs;

// Helper DTO for transferring tokens
public class TokenDTO {
    // Fields
    private String token;

    // Constructors
    public TokenDTO(String token) {
        this.token = token;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
