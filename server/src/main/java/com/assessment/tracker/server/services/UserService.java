package com.assessment.tracker.server.services;

import com.assessment.tracker.server.persistence.domain.User;
import com.assessment.tracker.server.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "User does not exist";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
       return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,USER_NOT_FOUND));
    }

    public boolean deleteUser(int id) {
        if(!userRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,USER_NOT_FOUND);
        userRepository.deleteById(id);
        return true;

    }

    public boolean deleteAllUsers(){
        userRepository.deleteAll();
        return true;
    }


    //IMPLEMENT get user by email,change password,

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //IMPLEMENT update user information

    public User updateUsername(String newUser, int id) {
        User currentUser = getUser(id);
        currentUser.setUsername(newUser);
        return userRepository.save(currentUser);

    }

    //implement encryption
    public User updateUserPassword(String newPassword, int id) {
        User currentUser= userRepository.findById(id).orElse(null);
        assert currentUser != null;
        currentUser.setPassword( newPassword );
        return userRepository.save(currentUser);
    }

    public User updateUserEmail(String newEmail, int id) {
        User currentUser= userRepository.findById(id).orElse(null);
        assert currentUser != null;
        currentUser.setEmail( newEmail );
        return userRepository.save(currentUser);
    }



}
