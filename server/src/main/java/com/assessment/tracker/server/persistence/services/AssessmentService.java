package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.app.mappers.AssessmentMapper;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AssessmentService {
    private final AssessmentRepo assessmentRepository;
    private final AssessmentMapper assessmentMapper;

    @Autowired
    public AssessmentService(AssessmentRepo assessmentRepository, AssessmentMapper assessmentMapper) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentMapper = assessmentMapper;
    }

    public void save(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    public AssessmentDTO getAssessmentByID(Integer id) {
        Assessment entity = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        System.out.println("entity: " + entity);
        return assessmentMapper.entityToApi(entity);
    }
}
