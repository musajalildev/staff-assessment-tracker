package com.assessment.tracker.server.app;

import com.assessment.tracker.server.api.controller.UserController;
import com.assessment.tracker.server.api.dto.*;
import com.assessment.tracker.server.api.dto.userHelperDTOs.*;
import com.assessment.tracker.server.app.mappers.UserMapper;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.enums.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.assessment.tracker.server.persistence.repos.UserRepository;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users") // corresponding to service
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;




    @Autowired
    public UserControllerImpl(UserService userService, UserMapper userMapper, UserRepository userRepository) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
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
            Authentication authentication,
            @RequestBody PasswordUpdDTO passwordData) {

        String requester = authentication.getName();
        User target = userService.getUser(id);
        if (target == null)
            return ResponseEntity.notFound().build();

        if (!requester.equals(target.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // or custom error
        }

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
            Authentication authentication,
            @RequestBody EmailUpdDTO updatedUserDTO) {

        String requester = authentication.getName();
        User existing = userService.getUser(id);
        if (existing == null)
            return ResponseEntity.notFound().build();

        if (!requester.equals(existing.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        String incomingEmail = updatedUserDTO.email;
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
            Authentication authentication,
            @RequestBody usernameUpdDTO updatedUserDTO) {

        String requester = authentication.getName();
        User existing = userService.getUser(id);
        String incomingUsername = updatedUserDTO.username;

        if (existing == null)
            return ResponseEntity.notFound().build();

        //ensure that requester is allowed to change username
        if (!requester.equals(existing.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // or custom error
        }

        // check that incoming data isnt blank
        if (incomingUsername.isBlank()) {
            System.out.println("Blank username");
            return ResponseEntity.badRequest().build();
        }

        User updated = userService.updateUsername(incomingUsername, id);
        return ResponseEntity.ok(userMapper.entityToApi(updated));
    }

    @PreAuthorize("hasAuthority(T(com.assessment.tracker.server.utils.enums.UserType).ROLE_TEACHING_SUPPORT)")
    @PutMapping("/{id}/permission")
    public ResponseEntity<UserDTO> updateUserPermission(
            @PathVariable UUID id,
            Authentication authentication,
            @RequestBody UserTypeUpdDTO updatedUserDTO) {
        User existing = userService.getUser(id);

        String requester = authentication.getName();
        System.out.println("Admin performing action: " + requester);

        // incomingUT (incoming user type :D)
        UserType incomingUT = updatedUserDTO.userType;
        if (existing == null)
            return ResponseEntity.notFound().build();

        User updated = userService.updateUserPermission(incomingUT, id);
        return new ResponseEntity<>(userMapper.entityToApi(updated), HttpStatus.OK);
    }

    //-----EXAM OFFICER OPERATIONS-----
    @PreAuthorize("hasAuthority(T(com.assessment.tracker.server.utils.enums.UserType).ROLE_EXAMS_OFFICER)")
    @PutMapping("/promote/{username}")
    public ResponseEntity<UserDTO> promoteUser(@PathVariable String username) {
        User toPromote= userService.getUserByUsername(username);
        UserType currentRole = toPromote.getUserType();

        switch(currentRole) {
            case ROLE_EXAMS_OFFICER -> {
                System.out.println("User is already an exams officer");
                return ResponseEntity.badRequest().build();
            }
            case ROLE_ACADEMIC -> {
                userService.updateUserPermission(UserType.ROLE_EXAMS_OFFICER, toPromote.getUserID());
                System.out.println("Promoted user to exams officer");
                return ResponseEntity.ok(userMapper.entityToApi(toPromote));
            }
            default -> {
                System.out.println("User is not an academic");
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @PreAuthorize("hasAuthority(T(com.assessment.tracker.server.utils.enums.UserType).ROLE_EXAMS_OFFICER)")
    @PutMapping("/demote/{username}")
    public ResponseEntity<String> demoteUser (@PathVariable String username, Authentication authentication) {

        String demoter = authentication.getName();

        if (!demoter.equals(username)) {
            User toDemote = userService.getUserByUsername(username);
            UserType currentRole = toDemote.getUserType();

            switch (currentRole){
                case ROLE_ACADEMIC -> {
                    System.out.println("Unable to demote to academic");
                    return new ResponseEntity<>("User is already an academic, demotion unapplicable"
                            , HttpStatus.BAD_REQUEST);
                }
                case ROLE_EXAMS_OFFICER -> {
                    System.out.println("Demoting user to academic");
                    userService.updateUserPermission(UserType.ROLE_ACADEMIC, toDemote.getUserID());
                    return new ResponseEntity<>("Demoted user to academic", HttpStatus.OK);
                }
                default -> {
                    System.out.println("User is not an exams officer");
                    return new ResponseEntity<>("User is not an exams officer", HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>("Unable to demote yourself", HttpStatus.BAD_REQUEST);

    }

    // -------------------- DELETE --------------------
    @PreAuthorize("hasAuthority(T(com.assessment.tracker.server.utils.enums.UserType).ROLE_TEACHING_SUPPORT)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {

        try {
            userService.getUser(id);
        } catch (ResponseStatusException e) {
            System.out.println("User not found");
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        boolean deleted = userService.deleteUser(id);
        return deleted
                ? ResponseEntity.ok("User deleted successfully.")
                : new ResponseEntity<>("Deletion not permitted", HttpStatus.FORBIDDEN);
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

// -------------------- CREATE --------------------
@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
    // Convert DTO to entity
    User user = new User(
        userDTO.getUsername(),
        userDTO.getEmail(),
        userDTO.getPassword(),
        userDTO.getUserType()
    );

    // Save to DB
    userRepository.save(user);

    return ResponseEntity.ok(userDTO);
}
}