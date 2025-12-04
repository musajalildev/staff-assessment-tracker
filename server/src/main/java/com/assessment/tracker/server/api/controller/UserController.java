package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.DTO.*;
import com.assessment.tracker.server.api.DTO.userHelperDTOs.*;
import com.assessment.tracker.server.api.DTO.authenticationDTOs.*;

import com.assessment.tracker.server.api.DTO.userHelperDTOs.PasswordUpdDTO;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.enums.*;
import com.assessment.tracker.server.utils.mappers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users") // corresponding to service

public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private String getUsernameByEmailOrUsername(String identifier) {
        User user = userService.getUserByEmail(identifier);
        if (user == null) {
            user = userService.getUserByUsername(identifier);
        }
        return user.getUsername();
    }

    // -------------------- READ --------------------
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                users.stream().map(userMapper::entityToApi).toList());
    }

    @GetMapping("/un/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return (user != null)
                ? ResponseEntity.ok(userMapper.entityToApi(user))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return (user != null)
                ? ResponseEntity.ok(userMapper.entityToApi(user))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return (user != null)
                ? ResponseEntity.ok(userMapper.entityToApi(user))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<String> getUserPermission(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserPermission(id).toString());
    }

    @GetMapping("/{permit}")
    public ResponseEntity<List<UserDTO>> getAllUsersByPermission(@PathVariable UserType permit) {
        return ResponseEntity.ok(
                userService.getAllUsersByPermission(permit)
                        .stream().map(userMapper::entityToApi).toList());
    }

    // -------------------- UPDATE --------------------
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updateUserPassword(@PathVariable UUID id,
            @RequestBody PasswordUpdDTO passwordData) {

        User target = userService.getUser(id);
        if (target == null)
            return ResponseEntity.notFound().build();

        if (!userService.validatePassword(passwordData.currentPassword, target.getPassword())) {
            return new ResponseEntity<>("Incorrect current password", HttpStatus.BAD_REQUEST);
        }

        return changeUserPassword(id, passwordData, target);
    }

    private ResponseEntity<String> changeUserPassword(UUID id, PasswordUpdDTO passwordInfo,
            User target) {
        if (target == null)
            return ResponseEntity.notFound().build();

        String currentPassword = target.getPassword();

        if (passwordInfo != null) {
            if (currentPassword.equals(passwordInfo.newPassword)) {
                return new ResponseEntity<>("updatedUser", HttpStatus.BAD_REQUEST);
            }
            userService.updateUserPassword(passwordInfo, id);
            return new ResponseEntity<>("Successfully updated password", HttpStatus.OK);
        }

        return new ResponseEntity<>("No valid incoming data", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<UserDTO> updateUserEmail(
            @PathVariable UUID id,
            @RequestBody EmailUpdDTO updatedUserDTO) {
        User existing = userService.getUser(id);
        String incomingEmail = updatedUserDTO.email;
        if (existing == null)
            return ResponseEntity.notFound().build();
        // check that incoming data isnt blank
        if (incomingEmail.isBlank()) {
            System.out.println("Blank email");
            return ResponseEntity.badRequest().build();
        }

        User updated = userService.updateUserEmail(incomingEmail, id);
        return ResponseEntity.ok(userMapper.entityToApi(updated));
    }

    @PutMapping("/{id}/username")
    public ResponseEntity<UserDTO> updateUsername(
            @PathVariable UUID id,
            @RequestBody usernameUpdDTO updatedUserDTO) {
        User existing = userService.getUser(id);
        String incomingUsername = updatedUserDTO.username;
        if (existing == null)
            return ResponseEntity.notFound().build();

        // check that incoming data isnt blank
        if (incomingUsername.isBlank()) {
            System.out.println("Blank username");
            return ResponseEntity.badRequest().build();
        }

        User updated = userService.updateUsername(incomingUsername, id);
        return ResponseEntity.ok(userMapper.entityToApi(updated));
    }

    @PutMapping("/{id}/permission")
    public ResponseEntity<UserDTO> updateUserPermission(
            @PathVariable UUID id,
            @RequestBody UserTypeUpdDTO updatedUserDTO) {
        User existing = userService.getUser(id);
        // incomingUT (incoming user type :D )
        UserType incomingUT = updatedUserDTO.userType;
        if (existing == null)
            return ResponseEntity.notFound().build();

        User updated = userService.updateUserPermission(incomingUT, id);
        return new ResponseEntity<>(userMapper.entityToApi(updated), HttpStatus.OK);
    }

    // -------------------- DELETE --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        boolean deleted = userService.deleteUser(id);
        return deleted
                ? ResponseEntity.ok("User deleted successfully.")
                : new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/wipe")
    public ResponseEntity<String> deleteAllUsers(@RequestParam String confirm) {
        if (!"YUTA_BUM".equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid confirmation value. Action not performed.");
        }
        userService.deleteAllUsers();
        return new ResponseEntity<>("All Users Erased", HttpStatus.OK);
    }
}
