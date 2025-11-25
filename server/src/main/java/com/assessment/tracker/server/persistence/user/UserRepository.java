package com.assessment.tracker.server.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {

    User findByUsername(String username);
    User findByEmail(String email);
    //altering methods to apply UUID

    User findByUserID(UUID userID);
    boolean existsByUserID(UUID id);
    void deleteByUserID(UUID id);

    //getting by common permission
    List<User> findAllByUserType(User.userType permission);
}
