package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.ModuleRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ModuleRole
 */
@Repository
public interface ModuleRolesRepo extends JpaRepository<ModuleRole, Integer> {
    Optional<Object> findById(UUID id);

    void deleteById(UUID id);
}
