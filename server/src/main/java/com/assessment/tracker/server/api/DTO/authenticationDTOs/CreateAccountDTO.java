package com.assessment.tracker.server.api.DTO.authenticationDTOs;

import com.assessment.tracker.server.utils.enums.*;

public class CreateAccountDTO {
    public String username;
    public String email;
    public String password;
    public userType userType;
    public AssesmentRole assesmentRole; //now assesment roles
    //public module role modRole
    public String assessment;


    public CreateAccountDTO(String username, String email, String password, userType userType, AssesmentRole role, String assessment){
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.assesmentRole = role;
        this.assessment = assessment;
    }

}
