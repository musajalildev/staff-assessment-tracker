package com.assessment.tracker.server.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assessment.tracker.server.persistence.entities.*;


@Repository
public interface ExamFeedbackRepo extends JpaRepository<ExamFeedback, Integer> {
    java.util.List<ExamFeedback> findByAssessment_ID(Integer assessmentID);
}
