package com.assessment.tracker.server.api.assessmentFeedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentFeedbackRepo extends JpaRepository<AssessmentFeedback, Integer> {
}
