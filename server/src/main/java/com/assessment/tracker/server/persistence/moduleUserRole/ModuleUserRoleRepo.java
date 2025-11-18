package com.assessment.tracker.server.persistence.moduleUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleUserRoleRepo extends JpaRepository<ModuleUserRole,Integer> {}
