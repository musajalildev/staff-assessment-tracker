package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.ModuleRole;

import com.assessment.tracker.server.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ModuleRole
 */
@Repository
public interface ModuleRolesRepo extends JpaRepository<ModuleRole, UUID> {
    List<ModuleRole> findAllByUser(User user);
}
