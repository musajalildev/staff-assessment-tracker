package com.assessment.tracker.server.api.DTO.authenticationDTOs;

import com.assessment.tracker.server.utils.enums.*;

public class CreateAccountDTO {
    public String username;
    public String email;
    public String password;
    public userType userType;

    public String assessment;// assessment involvement
    public AssesmentRole assesmentRole ;

    public String module; // module involvement
    public ModuleRole moduleRole;

    public CreateAccountDTO(String username, String email, String password, String assessment,
                            userType userType, ModuleRole moduleRole, AssesmentRole assesmentRole){
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.assessment = assessment;
        this.moduleRole = moduleRole;
        this.assesmentRole = assesmentRole;
    }

}
