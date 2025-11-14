package com.assessment.tracker.server.services;

import com.assessment.tracker.server.persistence.domain.AssignedUser;
import com.assessment.tracker.server.persistence.domain.Role;
import com.assessment.tracker.server.persistence.domain.User;
import com.assessment.tracker.server.persistence.repository.AssignedUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AssignedUserService {

    private static final String ROLE_NOT_FOUND = "Role does not exist";
    private final AssignedUserRepository assignedUserRepository;
    private  UserService userService;

    public AssignedUserService(AssignedUserRepository assignedUserRepository) {
        this.assignedUserRepository = assignedUserRepository;
    }

    //implement CRUD operations
    // -------------------- CREATE --------------------
    public AssignedUser createAssignment(AssignedUser assignedUser) {
        return assignedUserRepository.save(assignedUser);
    }

    //-------------------- READ --------------------
    //implement get assigned users w/o query params
    public List<AssignedUser> getAllAssignedUsers() {
        return assignedUserRepository.findAll();
    }

    //implement extracting assigned user by id and also finding out a user's role
    public AssignedUser getAssignedUser(int id) {
        return assignedUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,ROLE_NOT_FOUND));
    }

    public AssignedUser getUserAssignment( User user ) {
        return assignedUserRepository.findByUser(user);
    }

    //implement getting all users for a certain role
    public List<AssignedUser> getAllCommonRole( Role role){
        return assignedUserRepository.findAllByRole(role);
    }

    // -------------------- DELETE --------------------
    public void deleteAssignedUser(int id) {
        if(!assignedUserRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ROLE_NOT_FOUND);
        assignedUserRepository.deleteById(id);
    }

    // -------------------- UPDATE --------------------
    //implement update assigned user information

    //implement update user assignment using ID
    public AssignedUser updateUserAssignment(Role role, int id) {
        AssignedUser currentUser = getAssignedUser(id);
        currentUser.setRole(role);
        return assignedUserRepository.save(currentUser);
    }

    //implement update user assignment using email
    public AssignedUser updateUserAssignment(Role role, String email) {
        User currentUser= userService.getUserByEmail(email);
        return getAssignedUser(role, currentUser);

    }

    //implement update user assignment using username
    public AssignedUser updateUserAssignment( String username,Role role) {
        User currentUser= userService.getUserByUsername(username);
        return getAssignedUser(role, currentUser);
    }

    //helper method to extract user assignment info
    private AssignedUser getAssignedUser(Role role, User currentUser) {
        if(currentUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ROLE_NOT_FOUND);}

        AssignedUser currentAssignment = assignedUserRepository.findByUser(currentUser);
        if(currentAssignment.getRole() == role){
            return currentAssignment;
        }
        currentAssignment.setRole(role);
        return assignedUserRepository.save(currentAssignment);
    }

}
