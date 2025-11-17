package com.assessment.tracker.server.persistence.repository;
import com.assessment.tracker.server.persistence.domain.AssignedUser;
import com.assessment.tracker.server.persistence.domain.Role;
import com.assessment.tracker.server.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignedUserRepository extends JpaRepository<AssignedUser, Integer> {

    AssignedUser findByUser(User user);
    AssignedUser findByRole(Role role);
    boolean existsByUserAndRole(User user, Role role);
    List<AssignedUser> findAllByRole(Role role);
    List<AssignedUser> findAllByUser( User user);
}
