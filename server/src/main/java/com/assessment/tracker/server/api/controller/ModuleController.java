package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.Module;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

public interface ModuleController {
    @GetMapping(path= "/api/v1/module", consumes = "application/json")
    ResponseEntity<List<ModuleDTO>> getAllModules();

    @PutMapping(path = "/api/v1/module", consumes = "application/json")
    ResponseEntity<ModuleDTO> getFirstModuleByCode(int moduleCode);

    @PutMapping(path = "/api/v1/module", consumes = "application/json")
    ResponseEntity<Module> updateModule(UUID id, ModuleDTO dto);

    @PostMapping(path = "/api/v1/module", consumes = "application/json")
    ResponseEntity<ModuleDTO> createModule(ModuleDTO module);

    @PostMapping(path = "/api/v1/module/csv", consumes = "text/csv")
    ResponseEntity<ModuleDTO> createModuleCSV(@RequestBody InputStream csv);
}
