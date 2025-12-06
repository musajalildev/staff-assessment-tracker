package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.DTO.ModuleDTO;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.ModuleRepo;
import com.assessment.tracker.server.utils.mappers.ModuleMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepo moduleRepository;
    private ModuleMapper moduleMapper;

    public void update(UUID id, Module newModule) {
        Optional<Module> existingModule = Optional.ofNullable(moduleRepository.findById(id));
        if (existingModule.isPresent()) {
            newModule.setID(existingModule.get().getID());

            moduleRepository.save(newModule);
        } else {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
    }

    public void save(Module module) {
        moduleRepository.save(module);
    }

    public ModuleDTO getModuleDTOListByCode(int moduleCode) {
        Module moduleList = moduleRepository.findByCode(moduleCode);
        //if (moduleList.isEmpty()) { new RuntimeException("Module not found"); }

        //List<ModuleDTO> moduleDTOList = new ArrayList<>();
        //for (Module module : moduleList) {
            //moduleDTOList.add(moduleMapper.entityToApi(module));
        //}

        //return moduleDTOList;
        ModuleDTO moduleDTO = moduleMapper.entityToApi(moduleList);

        return moduleDTO;
    }

    public List<ModuleDTO> retrieveModules() {
        List<Module> moduleList = moduleRepository.findAll();
        List<ModuleDTO> moduleDTOList = new ArrayList<>();
        for (Module module : moduleList) {
            moduleDTOList.add(moduleMapper.entityToApi(module));
        }

        return moduleDTOList;
    }
}
