package com.assessment.tracker.server.app;


import com.assessment.tracker.server.app.mappers.AssessmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.services.*;

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
