package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.persistence.repos.AssignedUserRepository;
import com.assessment.tracker.server.utils.enums.userType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthorisedUser implements UserDetails {

    private final User user;
    private AssignedUserRepository aur;
    public AuthorisedUser(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        //base account type permissions
        userType baseRoleToAuth = user.getUserType();
        authorities.add(new SimpleGrantedAuthority(baseRoleToAuth.toString()));

        // Add assigned user roles if repository is available
        if (aur != null) {
            aur.findAllByUser(user)
                    .forEach(au -> authorities.add(
                            new SimpleGrantedAuthority(au.getRole().toString()))
                    );
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
