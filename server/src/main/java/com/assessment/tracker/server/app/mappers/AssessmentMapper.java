package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;

import com.assessment.tracker.server.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assessment.tracker.server.persistence.repos.ModuleRepo;
import com.assessment.tracker.server.persistence.repos.UserRepository;
import com.assessment.tracker.server.persistence.entities.Module;


/**
 * Mapper to convert between AssessmentDTO and Assessment
 */
@Component
public class AssessmentMapper implements Mapper<AssessmentDTO, Assessment> {

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private UserRepository userRepository;
    
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

   if (DTO.getModule() != null && DTO.getModule().getID() != null) {
    Module module = moduleRepo.findById(DTO.getModule().getID())
        .orElseThrow(() -> new RuntimeException("Module not found"));
    entity.setModule(module);
}



    //  Resolve setter
    if (DTO.getSetter() != null && DTO.getSetter().getUserID() != null) {
        User setter = userRepository.findByUserID(DTO.getSetter().getUserID());
        entity.addSetter(setter);
    }

    // Resolve checker
    if (DTO.getChecker() != null && DTO.getChecker().getUserID() != null) {
        User checker = userRepository.findByUserID(DTO.getChecker().getUserID());
        entity.addChecker(checker);
    }

    System.out.println("Mapping DTO to Entity:");
    System.out.println("Module ID: " + DTO.getModule().getID());
    System.out.println("Setter ID: " + (DTO.getSetter() != null ? DTO.getSetter().getUserID() : "null"));
    System.out.println("Checker ID: " + (DTO.getChecker() != null ? DTO.getChecker().getUserID() : "null"));


    return entity;

}
}
