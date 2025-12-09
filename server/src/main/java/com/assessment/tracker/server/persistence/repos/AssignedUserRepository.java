package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.*;

import com.assessment.tracker.server.utils.enums.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AssignedUserRepository extends JpaRepository<AssignedUser, Integer> {

    AssignedUser findByUser(User user);

    AssignedUser findByRole(AssesmentRole role);

    boolean existsByUserAndRole(User user, AssesmentRole role);

    List<AssignedUser> findAllByRole(AssesmentRole role);

    List<AssignedUser> findAllByUser(User user);
}
