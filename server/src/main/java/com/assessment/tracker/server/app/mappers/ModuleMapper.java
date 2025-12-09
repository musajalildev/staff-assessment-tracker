package com.assessment.tracker.server.utils.mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import com.assessment.tracker.server.api.DTO.*;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.Module;

@Component
public class ModuleMapper implements Mapper<ModuleDTO, Module> {
    @Autowired
    private AssessmentMapper assessmentMapper;
    private List<Assessment> assessmentList;
    private List<AssessmentDTO> assessmentDTOList;

    public ModuleDTO entityToApi(Module entity) {
        ModuleDTO DTO = new ModuleDTO();

        DTO.setID(entity.getID());
        DTO.setLeaderID(entity.getLeaderID());
        DTO.setCode(entity.getCode());
        DTO.setTitle(entity.getTitle());
        DTO.setArchived(entity.getArchiveStatus());

        assessmentList = entity.getAssessments();
        assessmentDTOList = new ArrayList<>();
        for (Assessment assessment : assessmentList) {
            assessmentDTOList.add(assessmentMapper.entityToApi(assessment));
        }
        DTO.setAssessments(assessmentDTOList);

        return DTO;
    }

    @Override
    public Module apiToEntity(ModuleDTO DTO) {
        Module entity = new Module();

        entity.setID(DTO.getID());
        entity.setLeaderID(DTO.getLeaderID());
        entity.setCode(DTO.getCode());
        entity.setTitle(DTO.getTitle());
        entity.setArchived(DTO.isArchived());

        assessmentDTOList = DTO.getAssessments();
        assessmentList = new ArrayList<>();
        for (AssessmentDTO assessmentDTO : assessmentDTOList) {
            assessmentList.add(assessmentMapper.apiToEntity(assessmentDTO));
        }
        entity.setAssessments(assessmentList);

        return entity;
    }
}
