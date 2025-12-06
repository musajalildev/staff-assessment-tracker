package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AssessmentFeedbackMapper implements Mapper<AssessmentFeedbackDTO, AssessmentFeedback> {
    
    private final UserMapper userMapper;

    @Autowired
    public AssessmentFeedbackMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public AssessmentFeedbackDTO entityToApi(AssessmentFeedback entity) {
        if (entity == null) return null;

        AssessmentFeedbackDTO dto = new AssessmentFeedbackDTO();
        dto.setId(entity.getID());
        dto.setFeedback(entity.getFeedback());
        dto.setCreatedDate(entity.getCreatedDate());
        
        if (entity.getAssessment() != null) {
            dto.setAssessmentID(entity.getAssessment().getID());
        }
        
        if (entity.getAuthor() != null) {
            dto.setAuthor(userMapper.entityToApi(entity.getAuthor()));
        }

        return dto;
    }

    @Override
    public AssessmentFeedback apiToEntity(AssessmentFeedbackDTO dto) {
        if (dto == null) return null;

        AssessmentFeedback entity = new AssessmentFeedback();
        entity.setID(dto.getId());
        entity.setFeedback(dto.getFeedback());
        entity.setCreatedDate(dto.getCreatedDate());
        
        // Note: Assessment and Author relationships should be set by the service
        // based on IDs, not here in the mapper

        return entity;
    }
}
