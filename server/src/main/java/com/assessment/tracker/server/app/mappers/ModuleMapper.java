package com.assessment.tracker.server.app.mappers;
import com.assessment.tracker.server.api.dto.AssessmentDTO;
import com.assessment.tracker.server.app.mappers.AssessmentMapper;
import com.assessment.tracker.server.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assessment.tracker.server.api.dto.ModuleDTO;
import java.util.List;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.Module;


@Component
public class ModuleMapper implements Mapper<ModuleDTO, Module> {

    private final AssessmentMapper assessmentMapper;

    private List<Assessment> assessmentList;
    private List<AssessmentDTO> assessmentDTOList;

    @Autowired
    public ModuleMapper(AssessmentMapper assessmentMapper) {
        this.assessmentMapper = assessmentMapper;
    }

    public ModuleDTO entityToApi(Module entity) {
        ModuleDTO DTO = new ModuleDTO();

        DTO.setID(entity.getID());
        DTO.setCode(entity.getCode());
        DTO.setTitle(entity.getTitle());
        DTO.setArchived(entity.getArchiveStatus());

        return DTO;
    }

    @Override
    public Module apiToEntity(ModuleDTO DTO) {
        Module entity = new Module();

        entity.setID(DTO.getID());
        entity.setCode(DTO.getCode());
        entity.setTitle(DTO.getTitle());
        entity.setArchived(DTO.isArchived());

        return entity;
    }
}
