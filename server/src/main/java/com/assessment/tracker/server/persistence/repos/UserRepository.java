package com.assessment.tracker.server.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import com.assessment.tracker.server.persistence.entities.*;

import com.assessment.tracker.server.utils.enums.*;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String email);
    // altering methods to apply UUID

    User findByUserID(UUID userID);

    boolean existsByUserID(UUID id);

    void deleteByUserID(UUID id);

    // getting by common permission
    List<User> findAllByUserType(UserType permission);
}
