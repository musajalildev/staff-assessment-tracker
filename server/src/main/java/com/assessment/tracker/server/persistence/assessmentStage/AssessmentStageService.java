package com.assessment.tracker.server.persistence.assessmentStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentStageService {
    private final AssessmentStageRepository assessmentStageRepository;

    @Autowired
    public AssessmentStageService(AssessmentStageRepository assessmentStageRepository) {
        this.assessmentStageRepository = assessmentStageRepository;
    }
}
