package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignedUserRepository extends JpaRepository<AssignedUser, Integer> {

    AssignedUser findByUser(User user);

    AssignedUser findByRole(Role role);

    boolean existsByUserAndRole(User user, Role role);

    List<AssignedUser> findAllByRole(Role role);

    List<AssignedUser> findAllByUser(User user);
}
