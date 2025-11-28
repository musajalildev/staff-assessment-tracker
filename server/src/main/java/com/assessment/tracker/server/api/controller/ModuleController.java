package com.assessment.tracker.server.api.controller;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import java.io.InputStream;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

public interface ModuleController {

        @GetMapping(path = "/api/v1/module", produces = "application/json", consumes = "application/json")
        ModuleDTO getModule(@RequestParam(required = false) String moduleCode);

        @PutMapping(path = "/api/v1/module", consumes = "application/json")
        ResponseEntity<ModuleDTO> updateModule(@RequestBody ModuleDTO module);

        @GetMapping(path = "/api/v1/module", produces = "application/json")
        List<ModuleDTO> getModules();

        @PostMapping(path = "/api/v1/module", consumes = "application/json")
        ResponseEntity<ModuleDTO> createModule(ModuleDTO module);

        @PostMapping(path = "/api/v1/module/csv", consumes = "text/csv")
        ResponseEntity<ModuleDTO> createModuleCSV(@RequestBody InputStream csv);
}
