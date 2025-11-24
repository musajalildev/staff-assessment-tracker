package com.assessment.tracker.server.api.assessment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {
    private final AssessmentRepo assessmentRepository;

    @Autowired
    public AssessmentService(AssessmentRepo assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public void save(Assessment assessment) {
        assessmentRepository.save(assessment);
    }
}
