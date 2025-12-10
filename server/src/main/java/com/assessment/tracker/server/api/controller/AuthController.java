package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.dto.authenticationDTOs.CreateAccountDTO;
import com.assessment.tracker.server.api.dto.authenticationDTOs.LoginDTO;
import com.assessment.tracker.server.api.dto.authenticationDTOs.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "User", description = "User-Related-Operations")
public interface AuthController {
    @PostMapping(
            path = "/signup",
            consumes = "application/json")
    @Operation(
            summary = "Signup into the system",
            description = "Creates a valid user in the db that returns a jwt token",
            tags = {"User"})
    ResponseEntity<TokenDTO> signup(@RequestBody CreateAccountDTO userInfoDTO);

    @PostMapping(
            path ="/login",
            consumes = "application/json")
    @Operation(
            summary = "Login into the system",
            description = "Checks your credentials are correct and returns a jwt token if valid",
            tags = {"Users"})
    ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO);
}
