package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;

import com.assessment.tracker.server.utils.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between AssessmentDTO and Assessment
 */
@Component
public class AssessmentMapper implements Mapper<AssessmentDTO, Assessment> {
    @Override
    public AssessmentDTO entityToApi(Assessment entity) {
        AssessmentDTO DTO = new AssessmentDTO();
        DTO.setID(entity.getID());
        DTO.setType(entity.getAssessmentType());
        DTO.setTitle(entity.getTitle());
        DTO.setProgress(entity.getProgress());
        // todo when merging user DTOs
        // DTO.setChecker(entity.getChecker());
        // DTO.setSetter(entity.getSetter());
        // DTO.setModule(entity.getModule());

        return DTO;
    }

    @Override
    public Assessment apiToEntity(AssessmentDTO DTO) {
        Assessment entity = new Assessment();
        entity.setID(DTO.getID());
        entity.setAssessmentType(DTO.getType());
        entity.setTitle(DTO.getTitle());
        entity.setProgress(DTO.getProgress());
        // todo when merging user entitys
        // entity.setChecker(DTO.getChecker());
        // entity.setSetter(DTO.getSetter());
        // entity.setModule(DTO.getModule());

        return entity;
    }
}
