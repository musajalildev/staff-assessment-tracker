package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.persistence.repos.AssessmentStageRepository;
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
