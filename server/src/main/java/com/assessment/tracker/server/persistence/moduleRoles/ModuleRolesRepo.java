package com.assessment.tracker.server.persistence.moduleRoles;

import com.assessment.tracker.server.persistence.module.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRolesRepo extends JpaRepository<Module, Integer> {}