package com.assessment.tracker.server.persistence.repos;

import com.assessment.tracker.server.persistence.entities.AssessmentStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentStageRepository extends JpaRepository<AssessmentStage, String> {
}
