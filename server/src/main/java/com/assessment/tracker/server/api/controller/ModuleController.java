package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.dto.ModuleDTO;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Module", description = "Module-Related-Operations")
public interface ModuleController {
        // -------------------- CREATE --------------------
        @PostMapping(path = "/api/module/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        ResponseEntity<Void> createModulesFromCSV(@RequestPart("csv") MultipartFile file, String uploader);

        // -------------------- READ --------------------
        @GetMapping(path = "/api/module/all", consumes = "application/json")
        ResponseEntity<List<ModuleDTO>> getAllModules();

        @PutMapping(path = "/api/module/", consumes = "application/json")
        ResponseEntity<ModuleDTO> getFirstModuleByCode(String moduleCode);

        // -------------------- UPDATE --------------------
        @PutMapping(path = "/api/module/update", consumes = "application/json")
        ResponseEntity<ModuleDTO> updateModule(UUID id, ModuleDTO dto);

        @PostMapping(path = "/api/module/create", consumes = "application/json")
        ResponseEntity<ModuleDTO> createModule(ModuleDTO module);

}
