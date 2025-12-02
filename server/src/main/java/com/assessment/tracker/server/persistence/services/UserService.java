package com.assessment.tracker.server.persistence.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND = "User does not exist";

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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

    public boolean deleteAllUsers() {
        userRepository.deleteAll();
        return true;
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

    public void updateUserPassword(String newPassword, UUID id) {
        User currentUser = userRepository.findByUserID(id);

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
