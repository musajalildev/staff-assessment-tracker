package com.assessment.tracker.server.api.dto.authenticationDTOs;

import com.assessment.tracker.server.utils.enums.*;

public class CreateAccountDTO {
    public String username;
    public String email;
    public String password;
    public UserType userType;

    public String assessment;// assessment involvement
    public AssessmentRole assessmentRole;

    public String moduleCode; // TODO: louis to change field to string
    public ModuleRoles moduleRole;

    public CreateAccountDTO(String username, String email, String password, String assessment,String moduleCode,
                            UserType userType, ModuleRoles moduleRole, AssessmentRole assessmentRole){
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;

        this.assessment = assessment;
        this.assessmentRole = assessmentRole;

        this.moduleCode = moduleCode;
        this.moduleRole = moduleRole;
    }

}
