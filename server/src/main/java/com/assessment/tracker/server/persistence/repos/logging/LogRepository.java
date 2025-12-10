package com.assessment.tracker.server.persistence.repos.logging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assessment.tracker.server.persistence.entities.logging.*;

/**
 * Repository for Log
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
}
