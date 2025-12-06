package com.assessment.tracker.server.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assessment.tracker.server.api.dto.*;

public interface AssessmentController {
        @GetMapping(
                produces = "application/json",
                path = "/api/assessment/{id}")
        @Operation(
                summary = "Get an assessment By Id",
                description = "Returns an assessment that matches the id given.")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<AssessmentDTO> getAssessment(@PathVariable int id);

        @PostMapping(
                consumes = "application/json",
                produces = "application/json",
                path = "/api/v1/assessment")
        @Operation(
                summary = "Creates assessment with given dto",
                description = "Returns the created Assessment DTO")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<AssessmentDTO> createAssessment(@RequestBody AssessmentDTO assessmentDTO);

        @PutMapping(
                consumes = "application/json",
                produces = "application/json",
                path = "/api/assessment/{id}")
        @Operation(
                summary = "Update assessment with updated assessment schema",
                description = "")
        @Tag(name = "Assessment", description = "Assessment-Related-Operations")
        ResponseEntity<AssessmentDTO> updateAssessment(
                        @PathVariable int id,
                        @RequestBody AssessmentDTO assessmentDTO);
}
