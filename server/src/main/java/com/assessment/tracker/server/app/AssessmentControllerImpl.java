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
    public ResponseEntity<List<AssessmentDTO>> getAllAssessments(Authentication auth) {
        User user = new User();
        // Attempts to get user object by querying rebo by username
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            // Returns unauthorised if user not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Checks the type of user
        if (!(user.getUserType() == UserType.ROLE_EXAMS_OFFICER
                || user.getUserType() == UserType.ROLE_TEACHING_SUPPORT)) {
            // Returns unauthorised if not correct type
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<AssessmentDTO> dtos = assessmentService.getAllAssessments();
        System.out.println(dtos);
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<AssessmentDTO> getAssessment(int id, Authentication auth) {
        // Object username =
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        AssessmentDTO dto = assessmentService.getAssessmentByID(id);
        System.out.println("DTO: " + dto);
        System.out.println("ID: " + id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<AssessmentDTO> createAssessment(AssessmentDTO assessmentDTO,
            Authentication auth) {
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!(user.getUserType() == UserType.ROLE_EXAMS_OFFICER
                || user.getUserType() == UserType.ROLE_TEACHING_SUPPORT)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
        }
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        AssessmentLog log = new AssessmentLog();
        log.setActionType(AssessmentActions.CREATE);
        log.setTargetAssessment(assessment);
        log.setPreviousState(null);
        log.setNewState(null);
        log.setUser(user);
        assessmentLogService.save(log);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }

    @Override
    public ResponseEntity<AssessmentDTO> updateAssessment(int id, AssessmentDTO assessmentDTO,
            Authentication auth) {
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        if (assessmentService.getAssessmentByID(assessment.getID()).getProgress() == AssessmentProgress.CHECKED) {
            if (!user.getCheckerFor().contains(assessment)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
            }
        } else if (assessmentService.getAssessmentByID(assessment.getID())
                .getProgress() == AssessmentProgress.SETTER_FORMAL_RESPONSE
                && !user.getSetterFor().contains(assessment)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
        } else if (assessmentService.getAssessmentByID(assessment.getID())
                .getProgress() == AssessmentProgress.EXAMS_OFFICER_CHECK
                && !(user.getUserType() == UserType.ROLE_EXAMS_OFFICER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
        } else if (assessmentService.getAssessmentByID(assessment.getID())
                .getProgress() == AssessmentProgress.EXTERNAL_EXAMINER_CHECK
                && !(user.getUserType() == UserType.ROLE_EXTERNAL_EXAMINER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
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

    @Override
    public ResponseEntity<AssessmentDTO> revertAssessment(int id, AssessmentDTO assessmentDTO,
            Authentication auth) {
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Assessment assessment = assessmentMapper.apiToEntity(assessmentDTO);
        if (assessmentService.getAssessmentByID(assessment.getID()).getProgress() == AssessmentProgress.CHECKED) {
            if (!(user.getUserType() == UserType.ROLE_TEACHING_SUPPORT
                    || user.getUserType() == UserType.ROLE_EXAMS_OFFICER)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
            }
        } else {
            if (!user.getSetterFor().contains(assessment)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(assessmentDTO);
            }
        }
        AssessmentLog log = new AssessmentLog();
        log.setActionType(AssessmentActions.REVERT);
        log.setTargetAssessment(assessment);
        log.setPreviousState(assessmentService.getAssessmentByID(assessment.getID()).getProgress());
        log.setNewState(assessment.getProgress());
        log.setUser(user);
        assessmentLogService.save(log);
        assessmentService.save(assessment);
        return ResponseEntity.ok(assessmentDTO);
    }
}
