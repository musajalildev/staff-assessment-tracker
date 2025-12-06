package com.assessment.tracker.server.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assessment.tracker.server.persistence.entities.*;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Integer> {
    Assessment findByID(Integer ID);
}
