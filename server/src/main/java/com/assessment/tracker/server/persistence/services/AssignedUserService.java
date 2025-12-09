package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.dto.AssessmentRolesDTO;
import com.assessment.tracker.server.app.mappers.AssessmentRoleMapper;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;

import com.assessment.tracker.server.utils.enums.*;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.ArrayList;

@Service
@Transactional
public class AssignedUserService {

    private static final String ROLE_NOT_FOUND = "Role does not exist";
    private final AssignedUserRepository assignedUserRepository;
    private final AssessmentRoleMapper assessmentRoleMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public AssignedUserService(AssignedUserRepository assignedUserRepository, UserService userService,
            UserRepository userRepository, AssessmentRoleMapper assessmentRoleMapper) {
        this.assignedUserRepository = assignedUserRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.assessmentRoleMapper = assessmentRoleMapper;
    }

    // implement CRUD operations
    // -------------------- CREATE --------------------

    //implement create assigned user by using user id and role
    public AssignedUser createAssignment(UUID userID, AssessmentRole role, Assessment assessment) {
        User currentUser= userService.getUser(userID);
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found for ID: " + userID);
        }

        // check if the role is valid
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        // prevent duplicates
        if (assignedUserRepository.existsByUserAndRole(currentUser, role)) {
            throw new IllegalStateException("User already has role: " + role);
        }
        AssignedUser assignedUser = new AssignedUser(currentUser, role, assessment);
        return assignedUserRepository.save(assignedUser);
    }

    //implement create assigned user by using username and role
    public AssignedUser createAssignment(String username, AssessmentRole role, Assessment assessment) {
        User target =findUserWithString(username);
        // check if the user exists
        assert target != null : "User not found for ID: " + username;
        // check if the role is valid
        assert role != null : "Role cannot be null";

        // prevent duplicates
        if (assignedUserRepository.existsByUserAndRole(target, role)) {
            throw new IllegalStateException("User already has role: " + role);
        }

        AssignedUser assignedUser = new AssignedUser(target, role, assessment);
        return assignedUserRepository.save(assignedUser);
    }

    // -------------------- READ --------------------
    // implement get assigned users w/o query params
    public List<AssessmentRolesDTO> getAllAssignedUsers() {
        List<AssignedUser> entities = assignedUserRepository.findAll();
        List<AssessmentRolesDTO> dtos = new ArrayList<>();
        for (AssignedUser entity : entities) {
            dtos.add(assessmentRoleMapper.entityToApi(entity));
        }
        return dtos;
    }

    // implement extracting assigned user by id and also finding out a user's role
    public AssignedUser getAssignedUser(int id) {
        return assignedUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ROLE_NOT_FOUND));
    }

    // implement extracting assigned user by username and also finding out a user's
    // role'
    // in this method username can be either email or username
    public List<AssignedUser> getAssignedUser(String username) {
        User currentUser = findUserWithString(username);
        List<AssignedUser> userAssignments = new ArrayList<>();
        for (AssignedUser au : assignedUserRepository.findAll()) {

            if (au.getUser().equals(currentUser)) {
                userAssignments.add(au);
            }
        }
        return userAssignments;
    }

    public List<AssessmentRole> getUserAssignment(UUID userid) {
        User currentUser= userRepository.findByUserID(userid);
        List<AssignedUser> currentAssignments = assignedUserRepository.findAllByUser(currentUser);
        List<AssessmentRole> roles = new ArrayList<>();

        for (AssignedUser au : currentAssignments) {
            roles.add(au.getRole());
        }

        return roles;
    }


    //implement getting all users for a certain role
    public List<AssignedUser> getAllCommonRole( AssessmentRole role){
        return assignedUserRepository.findAllByRole(role);
    }

    // -------------------- DELETE --------------------
    // implement delete assigned user by id
    public boolean deleteAssignedUser(int id) {
        if (!assignedUserRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ROLE_NOT_FOUND);

        assignedUserRepository.deleteById(id);
        return true;
    }

    // implement deleting all user-role assignments for a user
    public boolean deleteAllUserAssignments(UUID userid) {
        User currentUser = userService.getUser(userid);
        List<AssignedUser> currentAssignments = assignedUserRepository.findAllByUser(currentUser);
        if (currentAssignments.isEmpty()) {
            return false;
        }
        assignedUserRepository.deleteAll(currentAssignments);
        return true;
    }

    // IMPLEMENT DELETE ALL
    public boolean deleteAllAssignments() {
        assignedUserRepository.deleteAll();
        return true;
    }

    // -------------------- UPDATE --------------------
    // implement update assigned user information

    //implement update user assignment using ID
    public AssignedUser updateUserAssignment(AssessmentRole role, int id) {
        AssignedUser currentUser = getAssignedUser(id);
        currentUser.setRole(role);
        return assignedUserRepository.save(currentUser);
    }

    //implement update user assignment using email or username
    public AssignedUser updateUserAssignment(AssessmentRole role, String username) {
        User currentUser= findUserWithString(username);
        return getAssignedUser(role, currentUser);

    }


    //helper method to extract user assignment info
    private AssignedUser getAssignedUser(AssessmentRole role, User currentUser) {
        if(currentUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ROLE_NOT_FOUND);}

        AssignedUser currentAssignment = assignedUserRepository.findByUser(currentUser);
        if (currentAssignment.getRole() == role) {
            return currentAssignment;
        }
        currentAssignment.setRole(role);
        return assignedUserRepository.save(currentAssignment);
    }

    public User findUserWithString(String identifier) {
        User user = userService.getUserByEmail(identifier);
        if (user == null) {
            user = userService.getUserByUsername(identifier);
        }
        return user;
    }

}
