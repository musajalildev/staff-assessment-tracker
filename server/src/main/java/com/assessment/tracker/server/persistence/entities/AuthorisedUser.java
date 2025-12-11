package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.persistence.repos.AssignedUserRepository;
import com.assessment.tracker.server.persistence.repos.ModuleRolesRepo;
import com.assessment.tracker.server.utils.enums.UserType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of UserDetails with addtional roles
 */
public class AuthorisedUser implements UserDetails {

    private final User user;
    private AssignedUserRepository aur;
    private ModuleRolesRepo mrr;

    public AuthorisedUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // base account type permissions
        UserType baseRoleToAuth = user.getUserType();
        authorities.add(new SimpleGrantedAuthority(baseRoleToAuth.name()));

        // Add assigned user roles if repository is available
        if (aur != null) {
            aur.findAllByUser(user)
                    .forEach(au -> authorities.add(
                            new SimpleGrantedAuthority(au.getRole().name())));
        }

        // Add module roles if repository is available
        if(mrr != null) {
            mrr.findAllByUser(user)
                    .forEach(mr -> authorities.add(
                            new SimpleGrantedAuthority(mr.getRole().name())));
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

    public User getUser() {
        return user;
    }
}
