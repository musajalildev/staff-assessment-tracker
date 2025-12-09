package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.persistence.services.*;
import com.assessment.tracker.server.api.DTO.authenticationDTOs.*;
import com.assessment.tracker.server.persistence.entities.AuthorisedUser;

import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    public boolean loggedIn=false;

    public AuthController(AuthenticationManager manager,
                          TokenService tokenService,
                          UserService userService) {
        this.authenticationManager = manager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody CreateAccountDTO userInfoDTO) {
        TokenDTO t = userService.createUser(userInfoDTO);
        return (t != null) ? ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully ")
                            : ResponseEntity.badRequest().body("User Not Created Successfully ");
    }

    @PostMapping("/login")
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

}
