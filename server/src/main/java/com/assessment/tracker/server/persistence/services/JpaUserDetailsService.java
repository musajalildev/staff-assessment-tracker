package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.persistence.entities.AuthorisedUser;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

//class to CREATE an AuthorisedUser
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //method that loads user and secures them as AuthorisedUser(granted permissions based on a user type)
    public UserDetails loadUserByUsername(String identifier) {

        //ensure user exists whether by username or email
        User user = userRepository.findByUsername(identifier);
        String status="found by username";
        if (user == null) {
            user = userRepository.findByEmail(identifier);
            status="found by email";
        }
        if(user == null){
            throw new UsernameNotFoundException("User not found while using " + status);
        }
        return new AuthorisedUser(user);
        // AuthorisedUser implements UserDetails where user has a list of granted authorities

    }
}
