package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.dto.*;

import java.io.InputStream;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

public interface ModuleController {

        @GetMapping(
                path = "/api/v1/module",
                produces = "application/json",
                consumes = "application/json")
        @Operation(
                summary = "Gets module based on module code",
                description = "Returns respective module object")
        @Tag(name = "Module", description = "Module-Related-Operations")
        ModuleDTO getModule(@RequestParam(required = false) String moduleCode);

        @PutMapping(path = "/api/v1/module", consumes = "application/json")
        @Operation(
                summary = "Update module based on updated schema",
                description = "Returns respective module object")
        @Tag(name = "Module", description = "Module-Related-Operations")
        ResponseEntity<ModuleDTO> updateModule(@RequestBody ModuleDTO module);

        @GetMapping(path = "/api/v1/module", produces = "application/json")
        @Operation(
                summary = "Update module based on updated schema",
                description = "Returns respective module object")
        @Tag(name = "Module", description = "Module-Related-Operations")
        List<ModuleDTO> getModules();

        @PostMapping(path = "/api/v1/module", consumes = "application/json")
        @Operation(
                summary = "Create module based on schema",
                description = "Returns response entity if the creation is successful")
        @Tag(name = "Module", description = "Module-Related-Operations")
        ResponseEntity<ModuleDTO> createModule(ModuleDTO module);

        @PostMapping(path = "/api/v1/module/csv", consumes = "text/csv")
        @Operation(
                summary = "Create module based on CSV",
                description = "Returns whether the operation is successful")
        @Tag(name = "Module", description = "Module-Related-Operations")
        ResponseEntity<ModuleDTO> createModuleCSV(@RequestBody InputStream csv);
}
