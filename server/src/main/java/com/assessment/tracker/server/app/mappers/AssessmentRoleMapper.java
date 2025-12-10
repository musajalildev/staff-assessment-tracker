package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.api.dto.AssessmentRolesDTO;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;

import com.assessment.tracker.server.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between AssessmentRolesDTO and AssessmentRoles
 */
@Component
public class AssessmentRoleMapper implements Mapper<AssessmentRolesDTO, AssignedUser> {
    private final UserRepository userRepository;
    private final AssessmentRepo assessmentRepository;

    @Autowired
    public AssessmentRoleMapper(UserRepository userRepository, AssessmentRepo assessmentRepository) {
        this.userRepository = userRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public AssessmentRolesDTO entityToApi(AssignedUser entity) {
        AssessmentRolesDTO dto = new AssessmentRolesDTO(entity.getAssessment().getId(), entity.getUser().getUserId(),
                entity.getRole());
        return dto;
    }

    public AssignedUser apiToEntity(AssessmentRolesDTO dto) {
        AssignedUser entity = new AssignedUser();
        entity.setRole(dto.getRole());
        entity.setUser(userRepository.findByUserID(dto.getUserID()));
        entity.setAssessment(assessmentRepository.findByID(dto.getAssessmentID()));
        return entity;
    }
}
