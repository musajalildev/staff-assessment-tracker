package com.assessment.tracker.server.api.DTO.authenticationDTOs;

import com.assessment.tracker.server.utils.enums.*;

public class CreateAccountDTO {
    public String username;
    public String email;
    public String password;
    public UserType userType;
    public Role role; // now assesment roles
    // public module role modRole

    public CreateAccountDTO(String username, String email, String password, UserType userType, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.role = role;
    }

}
