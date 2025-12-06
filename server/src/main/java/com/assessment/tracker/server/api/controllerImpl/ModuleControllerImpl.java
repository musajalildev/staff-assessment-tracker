package com.assessment.tracker.server.api.controllerImpl;

import com.assessment.tracker.server.api.controller.ModuleController;
import com.assessment.tracker.server.api.DTO.ModuleDTO;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.services.ModuleService;
import com.assessment.tracker.server.utils.mappers.ModuleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
public class ModuleControllerImpl implements ModuleController {
    @Autowired
    private ModuleMapper moduleMapper;
    private ModuleService moduleService;

    @Override
    public ResponseEntity<List<ModuleDTO>> getAllModules() {
        List<ModuleDTO> dtos = moduleService.retrieveModules();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<ModuleDTO> getFirstModuleByCode(int moduleCode) {
        ModuleDTO dtos = moduleService.getModuleDTOListByCode(moduleCode);

        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<Module> updateModule(UUID id, ModuleDTO dto) {
        Module entity = moduleMapper.apiToEntity(dto);
        moduleService.update(id, entity);

        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<ModuleDTO> createModule(ModuleDTO module) {
        moduleService.save(moduleMapper.apiToEntity(module));
        return ResponseEntity.ok(module);
    }

    @Override
    public ResponseEntity<ModuleDTO> createModuleCSV(InputStream csv) {
        return null;
    }
}
