package com.assessment.tracker.server.services;

import com.assessment.tracker.server.api.module.ModuleController;
import com.assessment.tracker.server.api.module.ModuleDTO;
import com.assessment.tracker.server.persistence.module.ModuleService;
import com.assessment.tracker.server.services.mappers.ModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

@RestController
public class ModuleControllerImpl implements ModuleController {

    private final ModuleMapper moduleMapper;
    private final ModuleService moduleService;

    @Autowired
    public ModuleControllerImpl(ModuleService moduleService, ModuleMapper moduleMapper) {
        this.moduleMapper = moduleMapper;
        this.moduleService = moduleService;
    }

    @Override
    public ModuleDTO getModule(String moduleCode) {
        return null;
    }

    @Override
    public ResponseEntity<ModuleDTO> updateModule(ModuleDTO module) {
        return null;
    }

    @Override
    public List<ModuleDTO> getModules() {
        return List.of();
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
