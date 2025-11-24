package com.assessment.tracker.server.api.assessment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assessment.tracker.server.api.assessment.Assessment;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Integer> {
}
