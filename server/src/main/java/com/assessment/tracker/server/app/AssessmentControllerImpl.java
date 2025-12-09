package com.assessment.tracker.server.app;


import com.assessment.tracker.server.app.mappers.AssessmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.persistence.services.logging.AssessmentLogService;
import com.assessment.tracker.server.utils.enums.*;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.jwt.*;
import java.util.List;

@RestController
public class AssessmentControllerImpl implements AssessmentController {

    private final AssessmentService assessmentService;
    private final AssessmentLogService assessmentLogService;
    private final AssessmentMapper assessmentMapper;
    private final UserRepository userRepository;

    @Autowired
    public AssessmentControllerImpl(AssessmentService assessmentService, AssessmentMapper assessmentMapper,
            AssessmentLogService assessmentLogService,
            UserRepository userRepository) {
        this.assessmentService = assessmentService;
        this.assessmentMapper = assessmentMapper;
        this.assessmentLogService = assessmentLogService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<AssessmentDTO>> getAllAssessments() {
        // Object username =
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AssessmentDTO> dtos = assessmentService.getAllAssessments();
        System.out.println(dtos);
        return ResponseEntity.ok(dtos);
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
    public ResponseEntity<AssessmentDTO> createAssessment(AssessmentDTO assessmentDTO,
            AuthorisedUser user) {
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        AssessmentLog log = new AssessmentLog();
        log.setActionType(AssessmentActions.CREATE);
        log.setTargetAssessment(assessment);
        log.setPreviousState(null);
        log.setNewState(null);
        log.setUser(user.getUser());
        assessmentLogService.save(log);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }

    @Override
    public ResponseEntity<AssessmentDTO> updateAssessment(int id, AssessmentDTO assessmentDTO,
            Authentication auth) {
        User user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        if (assessmentService.getAssessmentByID(assessment.getID()).getProgress() == AssessmentProgress.CHECKED) {
            if (!user.getCheckerFor().contains(assessment)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
            }
        } else {
            if (!user.getSetterFor().contains(assessment)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
            }
        }
        AssessmentLog log = new AssessmentLog();
        log.setActionType(AssessmentActions.PROGRESS);
        log.setTargetAssessment(assessment);
        log.setPreviousState(assessmentService.getAssessmentByID(assessment.getID()).getProgress());
        log.setNewState(assessment.getProgress());
        log.setUser(user);
        assessmentLogService.save(log);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }
}
