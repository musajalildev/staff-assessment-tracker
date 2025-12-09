package com.assessment.tracker.server.api.DTO.authenticationDTOs;

import com.assessment.tracker.server.utils.enums.*;

public class CreateAccountDTO {
    public String username;
    public String email;
    public String password;
    public UserType userType;

    public String assessment;// assessment involvement
    public AssesmentRole assesmentRole ;

    public String moduleCode; // TODO: louis to change field to string
    public ModuleRole moduleRole;

    public CreateAccountDTO(String username, String email, String password, String assessment,String moduleCode,
                            UserType userType, ModuleRole moduleRole, AssesmentRole assesmentRole){
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;

        this.assessment = assessment;
        this.assesmentRole = assesmentRole;

        this.moduleCode = moduleCode;
        this.moduleRole = moduleRole;
    }

}
