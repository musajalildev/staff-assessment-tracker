package com.assessment.tracker.server.api.assessment;

import com.assessment.tracker.server.services.mappers.AssessmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.tracker.server.api.assessment.*;
import com.assessment.tracker.server.services.mappers.*;

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
    public ResponseEntity<AssessmentDTO> getAssessment(int id) {
        // Object username =
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssessmentDTO dto = assessmentService.getAssessmentByID(id);
        System.out.println("DTO: " + dto);
        System.out.println("ID: " + id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<AssessmentDTO> createAssessment(AssessmentDTO assessmentDTO) {
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }

    @Override
    public ResponseEntity<AssessmentDTO> updateAssessment(int id, AssessmentDTO assessmentDTO) {
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }
}
