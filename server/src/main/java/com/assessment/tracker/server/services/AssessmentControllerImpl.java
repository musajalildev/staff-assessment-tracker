package com.assessment.tracker.server.services;

import com.assessment.tracker.server.api.assessment.AssessmentController;
import com.assessment.tracker.server.api.assessment.AssessmentDTO;
import com.assessment.tracker.server.persistence.assessment.Assessment;
import com.assessment.tracker.server.persistence.assessment.AssessmentService;
import com.assessment.tracker.server.services.mappers.AssessmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssessmentControllerImpl implements AssessmentController {

    private final AssessmentService assessmentService;
    private final AssessmentMapper assessmentMapper;

    @Autowired
    public AssessmentControllerImpl(AssessmentService assessmentService, AssessmentMapper assessmentMapper) {
        this.assessmentService = assessmentService;
        this.assessmentMapper = assessmentMapper;
    }

    @Override
    public ResponseEntity<AssessmentDTO> getAssessments() {
        Object username = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return null;
    }

    @Override
    public ResponseEntity<AssessmentDTO> createAssessment(AssessmentDTO assessmentDTO) {
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }

    @Override
    public ResponseEntity<AssessmentDTO> updateAssessment(String assessmentId, AssessmentDTO assessmentDTO) {
        return null;
    }
}
