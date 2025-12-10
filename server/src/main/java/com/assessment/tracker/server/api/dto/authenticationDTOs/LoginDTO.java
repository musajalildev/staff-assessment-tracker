package com.assessment.tracker.server.api.dto.authenticationDTOs;

// Helper DTO for logging in 
public class LoginDTO {
    // Fields
    public String identifier;
    public String password;

    // Constructors
    public LoginDTO(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

}
