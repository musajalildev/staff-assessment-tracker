package com.assessment.tracker.server.services.mappers;

import com.assessment.tracker.server.api.assessment.Assessment;
import com.assessment.tracker.server.api.assessment.AssessmentDTO;
import com.assessment.tracker.server.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper implements Mapper<AssessmentDTO, Assessment> {
    @Override
    public AssessmentDTO entityToApi(Assessment entity) {
        AssessmentDTO DTO = new AssessmentDTO();
        DTO.setID(entity.getID());
        DTO.setType(entity.getAssessmentType());
        DTO.setTitle(entity.getTitle());
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
        // todo when merging user entitys
        // entity.setChecker(DTO.getChecker());
        // entity.setSetter(DTO.getSetter());
        // entity.setModule(DTO.getModule());

        return entity;
    }
}
