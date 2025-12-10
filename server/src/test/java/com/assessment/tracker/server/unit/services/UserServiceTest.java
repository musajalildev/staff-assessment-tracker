package com.assessment.tracker.server.unit.services;

import com.assessment.tracker.server.api.dto.authenticationDTOs.CreateAccountDTO;
import com.assessment.tracker.server.api.dto.authenticationDTOs.TokenDTO;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.JpaUserDetailsService;
import com.assessment.tracker.server.persistence.services.TokenService;
import com.assessment.tracker.server.persistence.services.UserService;
import com.assessment.tracker.server.utils.enums.AssessmentRole;
import com.assessment.tracker.server.utils.enums.ModuleRoles;
import com.assessment.tracker.server.utils.enums.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JpaUserDetailsService jpaUserDetailsService;
    @Mock private TokenService tokenService;
    @Mock private AssignedUserRepository assignedUserRepository;
    @Mock private AssessmentRepo assessmentRepo;
    @Mock private ModuleRepo moduleRepo;
    @Mock private ModuleRolesRepo moduleRolesRepo;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUser() {
        CreateAccountDTO account = new CreateAccountDTO(
                "username",
                "email",
                "pwd",
                "",
                "1",
                UserType.ROLE_ACADEMIC,
                ModuleRoles.ROLE_MODULE_STAFF,
                AssessmentRole.ROLE_CHECKER
        );
        TokenDTO token = userService.createUser(account);
    }
}
