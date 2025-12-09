package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;
import org.springframework.stereotype.Component;

@Component
public class AssessmentRoleMapper implements Mapper<AssessmentRolesDTO, AssignedUser> {
    private UserRepository userRepository;

    public AssessmentRoleMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AssessmentRolesDTO entityToApi(AssignedUser entity) {
        AssessmentRolesDTO dto = new AssessmentRolesDTO(entity.getAssignmentID(), entity.getUser().getUserID(),
                entity.getRole());
        return dto;
    }

    public AssignedUser apiToEntity(AssessmentRolesDTO dto) {
        AssignedUser entity = new AssignedUser();
        entity.setRole(dto.getRole());
        entity.setUser(userRepository.findByUserID(dto.getUserID()));
        entity.setAssignmentID(dto.getAssessmentID());
        return entity;
    }
}
