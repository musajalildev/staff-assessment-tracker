package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.dto.userHelperDTOs.PasswordUpdDTO;
import com.assessment.tracker.server.api.dto.authenticationDTOs.*;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;

import com.assessment.tracker.server.utils.enums.*;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsService detailsService;
    private final TokenService tokenService;
    private final AssignedUserRepository assignedUserRepository;
    public boolean authorised = false;

    private static final String USER_NOT_FOUND = "User does not exist";

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JpaUserDetailsService jpaUserDetailsService,
            TokenService tokenService, AssignedUserRepository assignedUserRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.detailsService = jpaUserDetailsService;
        this.tokenService = tokenService;
        this.assignedUserRepository = assignedUserRepository;
    }

    public TokenDTO createUser(CreateAccountDTO userinfo) {
        UserType base_role = userinfo.userType;

        User user = new User(userinfo.username, // username for an incoming account
                passwordEncoder.encode(userinfo.password), // encoded password
                userinfo.email, base_role); // email and role for an incoming account

        userRepository.save(user);
        User academic = userRepository.findByUsername(userinfo.username);
        // logic for academic role assignment
        if (base_role == UserType.ACADEMIC) {
            AssignedUser test = new AssignedUser(academic, userinfo.role);
            assignedUserRepository.save(test);
        }

        AuthorisedUser authorisedUser = (AuthorisedUser) detailsService.loadUserByUsername(userinfo.username);

        // succesful auth
        authorised = true;
        return tokenService.generateToken(authorisedUser.getAuthorities(), userinfo.username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(UUID id) {
        User focus = userRepository.findByUserID(id);
        if (focus == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        }
        return focus;
    }

    public boolean deleteUser(UUID id) {
        // TODO:check if user is an exam officer (deletion not allowed)
        if (!userRepository.existsByUserID(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        userRepository.deleteByUserID(id);
        return true;

    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    // IMPLEMENT get user by email,change password,

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserType getUserPermission(UUID id) {
        User user = getUser(id);
        return user.getUserType();
    }

    public List<User> getAllUsersByPermission(UserType permission) {
        return userRepository.findAllByUserType(permission);
    }

    // IMPLEMENT update user information

    public User updateUsername(String newUser, UUID id) {
        User currentUser = getUser(id);
        currentUser.setUsername(newUser);
        return userRepository.save(currentUser);

    }

    public void updateUserPassword(PasswordUpdDTO passwordInfo, UUID id) {
        // sets new password and encrypts it
        User currentUser = userRepository.findByUserID(id);
        String newPassword = passwordInfo.newPassword;
        assert currentUser != null;
        // encryption
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    public User updateUserEmail(String newEmail, UUID id) {
        User currentUser = userRepository.findByUserID(id);
        assert currentUser != null;
        currentUser.setEmail(newEmail);
        return userRepository.save(currentUser);
    }

    public User updateUserPermission(UserType newPermission, UUID id) {
        User currentUser = getUser(id);
        currentUser.setUserType(newPermission);
        return userRepository.save(currentUser);
    }

    // to be used in login service
    public boolean validatePassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

}
