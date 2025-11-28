package com.assessment.tracker.server.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assessment.tracker.server.persistence.entities.Module;

@Repository
public interface ModuleRepo extends JpaRepository<Module, Integer> {
}
