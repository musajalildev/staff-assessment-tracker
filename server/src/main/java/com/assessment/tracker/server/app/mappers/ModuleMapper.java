package com.assessment.tracker.server.app.mappers;
import com.assessment.tracker.server.api.dto.AssessmentDTO;
import com.assessment.tracker.server.app.mappers.AssessmentMapper;
import com.assessment.tracker.server.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import com.assessment.tracker.server.api.dto.ModuleDTO;
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
        ModuleDTO dto = new ModuleDTO();

        dto.setID(entity.getID());
        dto.setLeaderID(entity.getLeaderID());
        dto.setCode(entity.getCode());
        dto.setTitle(entity.getTitle());
        dto.setArchived(entity.getArchiveStatus());

        assessmentList = entity.getAssessments();
        assessmentDTOList = new ArrayList<>();
        for (Assessment assessment : assessmentList) {
            assessmentDTOList.add(assessmentMapper.entityToApi(assessment));
        }
        dto.setAssessments(assessmentDTOList);

        return dto;
    }

    @Override
    public Module apiToEntity(ModuleDTO dto) {
        Module entity = new Module();

        entity.setID(dto.getID());
        entity.setLeaderID(dto.getLeaderID());
        entity.setCode(dto.getCode());
        entity.setTitle(dto.getTitle());
        entity.setArchived(dto.isArchived());

        assessmentDTOList = dto.getAssessments();
        assessmentList = new ArrayList<>();
        for (AssessmentDTO assessmentDTO : assessmentDTOList) {
            assessmentList.add(assessmentMapper.apiToEntity(assessmentDTO));
        }
        entity.setAssessments(assessmentList);

        return entity;
    }
}
