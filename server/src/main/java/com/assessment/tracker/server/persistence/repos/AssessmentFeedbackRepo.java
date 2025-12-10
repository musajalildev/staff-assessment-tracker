package com.assessment.tracker.server.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assessment.tracker.server.persistence.entities.*;

/**
 * Repository for AssessmentFeedback
 */
@Repository
public interface AssessmentFeedbackRepo extends JpaRepository<AssessmentFeedback, Integer> {
    java.util.List<AssessmentFeedback> findByAssessment_Id(Integer assessmentID);
}
