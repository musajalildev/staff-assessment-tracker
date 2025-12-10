package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.*;

import com.assessment.tracker.server.utils.enums.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Assigned User
 */
public interface AssignedUserRepository extends JpaRepository<AssignedUser, Integer> {

    AssignedUser findByUser(User user);

    AssignedUser findByRole(AssessmentRole role);

    boolean existsByUserAndRole(User user, AssessmentRole role);

    List<AssignedUser> findAllByRole(AssessmentRole role);

    List<AssignedUser> findAllByUser(User user);
}
