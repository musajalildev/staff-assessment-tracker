package com.assessment.tracker.server.persistence.assessmentStage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentStageRepository extends JpaRepository<AssessmentStage, String> {
}
