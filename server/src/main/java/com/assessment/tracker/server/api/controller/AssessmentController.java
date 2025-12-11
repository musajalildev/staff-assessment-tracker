package com.assessment.tracker.server.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.*;

import java.util.List;

public interface AssessmentController {
        // -------------------- CREATE --------------------
        @PostMapping(consumes = "application/json", produces = "application/json", path = "/api/v1/assessment")
        @Operation(summary = "Creates assessment with given dto", description = "Returns the created Assessment DTO")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<AssessmentDTO> createAssessment(
                        @RequestBody AssessmentDTO assessmentDTO,
                        @AuthenticationPrincipal AuthorisedUser user);

        // -------------------- READ --------------------
        @GetMapping(produces = "application/json", path = "/api/assessment/all")
        @Operation(summary = "Get an assessment By Id", description = "Returns an assessment that matches the id given.")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<List<AssessmentDTO>> getAllAssessments();

        @GetMapping(produces = "application/json", path = "/api/assessment/{id}")
        ResponseEntity<AssessmentDTO> getAssessment(@PathVariable int id);

        // -------------------- UPDATE --------------------
        @PutMapping(consumes = "application/json", produces = "application/json", path = "/api/assessment/{id}")
        @Operation(summary = "Update assessment with updated assessment schema", description = "")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<AssessmentDTO> updateAssessment(
                        @PathVariable int id,
                        @RequestBody AssessmentDTO assessmentDTO,
                        Authentication auth);

        @PutMapping(consumes = "application/json", produces = "application/json", path = "/api/assessment/revert/{id}")
        @Operation(summary = "Reverts assessment to previous state", description = "")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<AssessmentDTO> revertAssessment(
                        @PathVariable int id,
                        @RequestBody AssessmentDTO assessmentDTO,
                        Authentication auth);
}
