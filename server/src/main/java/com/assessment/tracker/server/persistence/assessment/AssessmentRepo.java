package com.assessment.tracker.server.persistence.assessment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Integer> {}
