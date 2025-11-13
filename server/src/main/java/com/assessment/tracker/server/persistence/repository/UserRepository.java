package com.assessment.tracker.server.persistence.repository;

import com.assessment.tracker.server.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);
    User findByEmail(String email);
}
