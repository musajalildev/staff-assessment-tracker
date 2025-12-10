package com.assessment.tracker.server.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import com.assessment.tracker.server.persistence.entities.*;

import com.assessment.tracker.server.utils.enums.*;

/**
 * Repository for User
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String email);
    // altering methods to apply UUID

    User findByUserId(UUID userID);

    boolean existsByUserId(UUID id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void deleteByUserId(UUID id);

    // getting by common permission
    List<User> findAllByUserType(UserType permission);
}
