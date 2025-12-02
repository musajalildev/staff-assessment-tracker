package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

/** @noinspection DuplicatedCode */
@RestController
@RequestMapping("/assign")
public class AssignedUserController {

    private final AssignedUserService assignedUserService;
    private final UserService userService;

    @Autowired
    public AssignedUserController(AssignedUserService assignedUserService, UserService userService) {
        this.assignedUserService = assignedUserService;
        this.userService = userService;
    }

    // implement CRUD operations
    // -------------------- CREATE --------------------
    // implement get assigned users w/o query params
    @PostMapping({ "", "/" })
    public ResponseEntity<AssignedUser> createAssignment(@RequestBody UUID userID, @RequestBody Role role) {
        AssignedUser createdUser = assignedUserService.createAssignment(userID, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // -------------------- READ --------------------
    @GetMapping
    public ResponseEntity<List<AssignedUser>> getAllAssignedUsers() {
        List<AssignedUser> assignedUsers = assignedUserService.getAllAssignedUsers();
        return (assignedUsers != null) ? ResponseEntity.ok(assignedUsers)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{role}")
    public ResponseEntity<List<AssignedUser>> getAllCommonRole(@PathVariable Role role) {
        List<AssignedUser> commonUsers = assignedUserService.getAllCommonRole(role);
        return (commonUsers != null) ? ResponseEntity.ok(commonUsers)
                : ResponseEntity.notFound().build();

    }

    // get user-role assignment data by assignment id, path variable
    @GetMapping("/id/{id}")
    public ResponseEntity<AssignedUser> getAssignedUser(@PathVariable int id) {
        AssignedUser assignedUser = assignedUserService.getAssignedUser(id);
        return (assignedUser != null) ? ResponseEntity.ok(assignedUser)
                : ResponseEntity.notFound().build();
    }

    // get user-role assignment by email/username, path variable
    // will allow the service method to check if string is present
    // as email or username
    @GetMapping("/un/{username}")
    public ResponseEntity<List<AssignedUser>> getAssignedUserByUsername(@PathVariable String username) {
        List<AssignedUser> assignedUser = assignedUserService.getAssignedUser(username);
        return (assignedUser != null) ? ResponseEntity.ok(assignedUser)
                : ResponseEntity.notFound().build();
    }

    // GET ALL USER ROLES (notice mapping pattern)
    // get all user roles by ID
    @GetMapping("/role/userid/{id}")
    public ResponseEntity<List<Role>> getAllUserRolesById(@PathVariable UUID id) {
        List<Role> userRoles = assignedUserService.getUserAssignment(id);
        return (userRoles != null) ? ResponseEntity.ok(userRoles)
                : ResponseEntity.notFound().build();
    }

    // get an assignment role enum by assignmentID
    @GetMapping("/role/assignmentID/{id}")
    public ResponseEntity<Role> getRoleByAssignmentId(@PathVariable int id) {
        AssignedUser assignedUser = assignedUserService.getAssignedUser(id);
        Role role = assignedUser.getRole();
        return (role != null) ? ResponseEntity.ok(role)
                : ResponseEntity.notFound().build();
    }

    // get all roles for a user
    @GetMapping("/role/{username}")
    public ResponseEntity<List<Role>> getAllUserRoles(@PathVariable String username) {
        User user = assignedUserService.findUserWithString(username);
        List<Role> userRoles = assignedUserService.getUserAssignment(user.getUserID());
        return (userRoles != null) ? ResponseEntity.ok(userRoles)
                : ResponseEntity.notFound().build();
    }

    // -------------------- UPDATE --------------------
    // implement update user information

    // implement update user assignment using assignmentID
    @PutMapping("/role/id/{id}")
    public ResponseEntity<AssignedUser> updateUserRole(@PathVariable int id, @RequestBody Role role) {
        AssignedUser currentUserAssignment = assignedUserService.getAssignedUser(id);
        if (currentUserAssignment != null)

            if (currentUserAssignment.getRole() == role) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                currentUserAssignment.setRole(role);
                return ResponseEntity.ok(assignedUserService.updateUserAssignment(role, id));
            }
        return ResponseEntity.notFound().build();
    }

    // ----------------------DELETE----------------
    // implement deletion of user assignment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignedUser(@PathVariable int id) {
        String name = assignedUserService.getAssignedUser(id).getUser().getUsername();
        String role = assignedUserService.getAssignedUser(id).getRole().toString();
        boolean deleted = assignedUserService.deleteAssignedUser(id);
        return (deleted) ? new ResponseEntity<>(
                "Name: " + name +
                        "\n Role: " + role +
                        "\n User Role Assignment Erased",
                HttpStatus.OK)
                : new ResponseEntity<>("User Role Assignment Not Found", HttpStatus.NOT_FOUND);

    }

    // delete all assignments for a user
    @DeleteMapping("/user/{userid}")
    public ResponseEntity<String> deleteUserAssignments(@PathVariable UUID userid) {
        String username = userService.getUser(userid).getUsername();
        boolean deleted = assignedUserService.deleteAllUserAssignments(userid);
        return (deleted) ? new ResponseEntity<>(
                "User: " + username +
                        "\n Role Assignments Erased",
                HttpStatus.OK)
                : new ResponseEntity<>("All User Role Assignments Not Found", HttpStatus.NOT_FOUND);
    }

    // delete all. use carefully :)
    @DeleteMapping("/wipe")
    public ResponseEntity<String> deleteAllAssignments(@RequestParam String confirm) {
        boolean deleted = assignedUserService.deleteAllAssignments();
        if (!"DELETE_EVERYTHING".equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid confirmation value. Action not performed.");
        }
        assignedUserService.deleteAllAssignments();
        return ResponseEntity.ok("All data deleted.");
    }

}
