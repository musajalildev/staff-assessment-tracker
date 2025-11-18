package com.assessment.tracker.server.api.assessment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AssessmentController {
    @GetMapping(
            produces = "application/json",
            path = "/api/v1/assessment"
    )
    ResponseEntity<AssessmentDTO> getAssessments();

    @PostMapping(
            consumes = "application/json",
            produces = "application/json",
            path ="/api/v1/assessment"
    )
    ResponseEntity<AssessmentDTO> createAssessment(@RequestBody AssessmentDTO assessmentDTO);

    @PutMapping(
            consumes = "application/json",
            produces = "application/json",
            path = "/api/v1/assessment/{assessmentId}"
    )
    ResponseEntity<AssessmentDTO> updateAssessment(
            @PathVariable("assessmentId") String assessmentId,
            @RequestBody AssessmentDTO assessmentDTO);
}
