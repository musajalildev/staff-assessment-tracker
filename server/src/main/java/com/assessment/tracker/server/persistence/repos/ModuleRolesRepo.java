package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.Module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRolesRepo extends JpaRepository<Module, Integer> {
}
