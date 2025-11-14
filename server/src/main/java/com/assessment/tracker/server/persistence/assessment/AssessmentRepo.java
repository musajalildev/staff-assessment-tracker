package com.assessment.tracker.server.persistence.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepo extends JpaRepository<Assessment, Integer> {}
