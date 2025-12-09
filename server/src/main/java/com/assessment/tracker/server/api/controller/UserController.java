package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.dto.UserDTO;
import com.assessment.tracker.server.api.dto.userHelperDTOs.EmailUpdDTO;
import com.assessment.tracker.server.api.dto.userHelperDTOs.PasswordUpdDTO;
import com.assessment.tracker.server.api.dto.userHelperDTOs.UserTypeUpdDTO;
import com.assessment.tracker.server.api.dto.userHelperDTOs.usernameUpdDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "User", description = "User-Related-Operations")
public interface UserController {

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users in the system",
            tags = {"User"}
    )
    ResponseEntity<List<UserDTO>> getAllUsers();

    @Operation(
            summary = "Get user by username",
            description = "Retrieve a user by their unique username",
            tags = {"User"}
    )
    ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Username of the user") @PathVariable String username
    );

    @Operation(
            summary = "Get user by email",
            description = "Retrieve a user by their unique email",
            tags = {"User"}
    )
    ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "Email of the user") @PathVariable String email
    );

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user by their unique ID",
            tags = {"User"})
    ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of the user") @PathVariable UUID id
    );

    @Operation(
            summary = "Get user permissions",
            description = "Retrieve the permissions of a specific user by ID",
            tags = {"User"})
    ResponseEntity<String> getUserPermission(
            @Parameter(description = "ID of the user") @PathVariable UUID id
    );

    @Operation(
            summary = "Update user password",
            description = "Update the password of a specific user by ID",
            tags = {"User"})
    ResponseEntity<String> updateUserPassword(
            @Parameter(description = "ID of the user") @PathVariable UUID id,
            @Parameter(description = "New password data") @RequestBody PasswordUpdDTO passwordData
    );

    @Operation(
            summary = "Update user email",
            description = "Update the email of a specific user by ID",
            tags = {"User"})
    ResponseEntity<UserDTO> updateUserEmail(
            @Parameter(description = "ID of the user") @PathVariable UUID id,
            @Parameter(description = "Updated email data") @RequestBody EmailUpdDTO updatedUserDTO
    );

    @Operation(
            summary = "Update username",
            description = "Update the username of a specific user by ID",
            tags = {"User"})
    ResponseEntity<UserDTO> updateUsername(
            @Parameter(description = "ID of the user") @PathVariable UUID id,
            @Parameter(description = "Updated username data") @RequestBody usernameUpdDTO updatedUserDTO
    );

    @Operation(
            summary = "Update user permission",
            description = "Update the permission type of a specific user by ID",
            tags = {"User"})
    ResponseEntity<UserDTO> updateUserPermission(
            @Parameter(description = "ID of the user") @PathVariable UUID id,
            @Parameter(description = "Updated permission data") @RequestBody UserTypeUpdDTO updatedUserDTO
    );

    @Operation(
            summary = "Delete user",
            description = "Delete a specific user by their ID",
            tags = {"User"})
    ResponseEntity<String> deleteUser(
            @Parameter(description = "ID of the user") @PathVariable UUID id
    );
}
