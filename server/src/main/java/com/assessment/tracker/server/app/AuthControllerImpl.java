package com.assessment.tracker.server.app;

import com.assessment.tracker.server.api.controller.AuthController;
import com.assessment.tracker.server.persistence.services.*;
import com.assessment.tracker.server.api.dto.authenticationDTOs.*;
import com.assessment.tracker.server.persistence.entities.AuthorisedUser;

import com.assessment.tracker.server.utils.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    public boolean loggedIn = false;

    @Autowired
    public AuthControllerImpl(AuthenticationManager manager,
                          TokenService tokenService,
                          UserService userService) {
        this.authenticationManager = manager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority(T(com.assessment.tracker.server.utils.enums.UserType).ROLE_TEACHING_SUPPORT)")
    //user creation
    public ResponseEntity<TokenDTO> signup(@RequestBody CreateAccountDTO userInfoDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userInfoDTO));
    }

    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginInfo){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginInfo.identifier,
                        loginInfo.password
                )
        );
        //create an authorised version of user
        AuthorisedUser securityUser = (AuthorisedUser) authentication.getPrincipal();

        //generate token for user login session
        TokenDTO token = tokenService.generateToken(
                securityUser.getAuthorities(),
                securityUser.getUsername()
        );
        loggedIn = true;

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("Logged out successfully.");
    }

}
