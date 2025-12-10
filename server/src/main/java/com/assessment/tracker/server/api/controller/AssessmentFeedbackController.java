package com.assessment.tracker.server.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assessment.tracker.server.api.dto.AssessmentFeedbackDTO;

import java.util.List;

public interface AssessmentFeedbackController {
        // -------------------- CREATE --------------------
        @PostMapping(consumes = "application/json", produces = "application/json", path = "/assessments/{assessmentId}/feedback")
        @Operation(summary = "Create feedback for an assessment", description = "Create feedback based on the assessment it"
                        +
                        " relates to and the schema of the new feedback")
        @Tag(name = "Feedback", description = "Feedback-Related-Operations")
        ResponseEntity<AssessmentFeedbackDTO> createFeedback(
                        @PathVariable Integer assessmentId,
                        @RequestBody AssessmentFeedbackDTO feedbackDTO);

        // -------------------- READ --------------------
        @GetMapping(produces = "application/json", path = "/assessments/{assessmentId}/feedback")
        @Operation(summary = "Gets list of feedback based on the assessment id", description = "Returns a list of feedback given related to a respective assessment id")
        @Tag(name = "Feedback", description = "Feedback-Related-Operations")
        ResponseEntity<List<AssessmentFeedbackDTO>> getFeedbackByAssessment(@PathVariable Integer assessmentId);

        @GetMapping(produces = "application/json", path = "/assessments/feedback/{feedbackId}")
        @Operation(summary = "Get feedback object based on id", description = "Get feedback object based on id")
        @Tag(name = "Feedback", description = "Feedback-Related-Operations")
        ResponseEntity<AssessmentFeedbackDTO> getFeedbackById(@PathVariable Integer feedbackId);

        // -------------------- UPDATE --------------------
        @PutMapping(consumes = "application/json", produces = "application/json", path = "/assessments/feedback/{feedbackId}")
        @Operation(summary = "update feedback based on feedbackId and new schema", description = "Update feedback based on an updated schema")
        @Tag(name = "Feedback", description = "Feedback-Related-Operations")
        ResponseEntity<AssessmentFeedbackDTO> updateFeedback(
                        @PathVariable Integer feedbackId,
                        @RequestBody AssessmentFeedbackDTO feedbackDTO);

        // -------------------- DELETE--------------------
        @DeleteMapping(path = "/assessments/feedback/{feedbackId}")
        @Operation(summary = "Delete feedback based on Id", description = "Delete assessment feedback based on assessment id")
        @Tag(name = "Feedback", description = "Feedback-Related-Operations")
        ResponseEntity<Void> deleteFeedback(@PathVariable Integer feedbackId);
}
