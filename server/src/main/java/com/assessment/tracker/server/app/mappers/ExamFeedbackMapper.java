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
import org.springframework.beans.factory.annotation.Autowired;
import com.assessment.tracker.server.api.DTO.ExamFeedbackDTO;

@Component
public class ExamFeedbackMapper implements Mapper<ExamFeedbackDTO, ExamFeedback> {

    private final UserMapper userMapper;

    @Autowired
    public ExamFeedbackMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ExamFeedbackDTO entityToApi(ExamFeedback entity) {
        if (entity == null)
            return null;

        ExamFeedbackDTO dto = new ExamFeedbackDTO();
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
    public ExamFeedback apiToEntity(ExamFeedbackDTO dto) {
        if (dto == null)
            return null;

        ExamFeedback entity = new ExamFeedback();
        entity.setID(dto.getId());
        entity.setFeedback(dto.getFeedback());
        entity.setCreatedDate(dto.getCreatedDate());
        // Note: Assessment and Author relationships should be set by the service
        // based on IDs, not here in the mapper
        return entity;
    }
}
