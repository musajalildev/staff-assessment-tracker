package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.Module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Module
 */
@Repository
public interface ModuleRepo extends JpaRepository<Module, Integer> {
    Module findById(UUID id);

    Module findByCode(String code);
}
