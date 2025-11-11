package com.assessment.tracker.server.persistence.module;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepo extends JpaRepository<Module, Integer> {
}
