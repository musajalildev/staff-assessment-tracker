package com.assessment.tracker.server.api.dto.authenticationDTOs;

public class LoginDTO {
    public String identifier;
    public String password;

    public LoginDTO(String identifier,String password){
        this.identifier = identifier;
        this.password = password;
    }

}
