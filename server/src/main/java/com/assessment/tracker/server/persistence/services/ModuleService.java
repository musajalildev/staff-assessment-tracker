package com.assessment.tracker.server.persistence.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;
import com.assessment.tracker.server.app.mappers.ModuleMapper;
import com.assessment.tracker.server.utils.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleService {

    private final ModuleRepo moduleRepository;
    private final ModuleMapper moduleMapper;

    @Autowired
    public ModuleService(ModuleMapper moduleMapper, ModuleRepo moduleRepository) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
    }

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
        Module module = moduleRepository.findByCode(moduleCode);
        ModuleDTO moduleDTO = moduleMapper.entityToApi(module);

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
