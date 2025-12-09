package com.assessment.tracker.server.api.controllerImpl;

import com.assessment.tracker.server.api.controller.ModuleController;
import com.assessment.tracker.server.api.DTO.ModuleDTO;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.services.ModuleService;
import com.assessment.tracker.server.utils.mappers.ModuleMapper;
import com.assessment.tracker.server.utils.mappers.CsvMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ModuleControllerImpl implements ModuleController {
    @Autowired
    private ModuleMapper moduleMapper;
    private ModuleService moduleService;
    private CsvMapper csvMapper;

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
    public ResponseEntity<Void> createModulesFromCSV(@RequestPart("csv") MultipartFile file, String uploader) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        if (!originalName.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().build();
        }

        List<Module> newModuleList = csvMapper.apiToEntity(file);
        return (ResponseEntity<Void>) ResponseEntity.ok();
    }
}
