package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.dto.AssessmentRolesDTO;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.AssignedUser;
import com.assessment.tracker.server.utils.enums.AssessmentRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Assignment", description = "User Role Assignment Operations")
@RequestMapping("/assign")
public interface AssignedUserController {

        // -------------------- CREATE --------------------
        @Operation(summary = "Create a user assignment", description = "Assign a role to a user by their UUID")
        @PostMapping({ "", "/" })
        ResponseEntity<AssignedUser> createAssignment(
                        @Parameter(description = "ID of the user") @RequestBody UUID userID,
                        @Parameter(description = "Role to assign") @RequestBody AssessmentRole role,
                        Assessment assessment);

        // -------------------- READ --------------------
        @Operation(summary = "Get all assigned users", description = "Retrieve all user-role assignments")
        @GetMapping
        ResponseEntity<List<AssessmentRolesDTO>> getAllAssignedUsers();

        @Operation(summary = "Get all users with a specific role", description = "Retrieve assignments filtered by role")
        @GetMapping("/{role}")
        ResponseEntity<List<AssignedUser>> getAllCommonRole(
                        @Parameter(description = "Role to filter") @PathVariable AssessmentRole role);

        @Operation(summary = "Get assignment by ID", description = "Retrieve assignment data by assignment ID")
        @GetMapping("/id/{id}")
        ResponseEntity<AssignedUser> getAssignedUser(
                        @Parameter(description = "Assignment ID") @PathVariable int id);

        @Operation(summary = "Get assignments by username", description = "Retrieve all assignments for a user by username or email")
        @GetMapping("/un/{username}")
        ResponseEntity<List<AssignedUser>> getAssignedUserByUsername(
                        @Parameter(description = "Username or email") @PathVariable String username);

        @Operation(summary = "Get all roles for a user by ID", description = "Retrieve all roles assigned to a user by UUID")
        @GetMapping("/role/userid/{id}")
        ResponseEntity<List<AssessmentRole>> getAllUserRolesById(
                        @Parameter(description = "User ID") @PathVariable UUID id);

        @Operation(summary = "Get role by assignment ID", description = "Retrieve a role assigned for a specific assignment")
        @GetMapping("/role/assignmentID/{id}")
        ResponseEntity<AssessmentRole> getRoleByAssignmentId(
                        @Parameter(description = "Assignment ID") @PathVariable int id);

        @Operation(summary = "Get all roles for a user by username", description = "Retrieve all roles for a user identified by username or email")
        @GetMapping("/role/{username}")
        ResponseEntity<List<AssessmentRole>> getAllUserRoles(
                        @Parameter(description = "Username or email") @PathVariable String username);

        // -------------------- UPDATE --------------------
        @Operation(summary = "Update a user's role", description = "Update role of an assignment by ID")
        @PutMapping("/role/id/{id}")
        ResponseEntity<AssignedUser> updateUserRole(
                        @Parameter(description = "Assignment ID") @PathVariable int id,
                        @Parameter(description = "New role") @RequestBody AssessmentRole role);

        // -------------------- DELETE --------------------
        @Operation(summary = "Delete an assignment by ID", description = "Delete a specific assignment")
        @DeleteMapping("/{id}")
        ResponseEntity<String> deleteAssignedUser(
                        @Parameter(description = "Assignment ID") @PathVariable int id);

        @Operation(summary = "Delete all assignments for a user", description = "Delete all role assignments for a specific user by UUID")
        @DeleteMapping("/user/{userid}")
        ResponseEntity<String> deleteUserAssignments(
                        @Parameter(description = "User ID") @PathVariable UUID userid);

}