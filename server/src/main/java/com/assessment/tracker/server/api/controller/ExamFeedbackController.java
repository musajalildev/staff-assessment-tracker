package com.assessment.tracker.server.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assessment.tracker.server.api.DTO.AssessmentFeedbackDTO;

import java.util.List;

public interface ExamFeedbackController {
    @GetMapping(produces = "application/json", path = "/assessments/{assessmentId}/feedback")
    ResponseEntity<List<AssessmentFeedbackDTO>> getFeedbackByAssessment(@PathVariable Integer assessmentId);

    @GetMapping(produces = "application/json", path = "/assessments/feedback/{feedbackId}")
    ResponseEntity<AssessmentFeedbackDTO> getFeedbackById(@PathVariable Integer feedbackId);

    @PostMapping(consumes = "application/json", produces = "application/json", path = "/assessments/{assessmentId}/feedback")
    ResponseEntity<AssessmentFeedbackDTO> createFeedback(
            @PathVariable Integer assessmentId,
            @RequestBody AssessmentFeedbackDTO feedbackDTO);

    @PutMapping(consumes = "application/json", produces = "application/json", path = "/assessments/feedback/{feedbackId}")
    ResponseEntity<AssessmentFeedbackDTO> updateFeedback(
            @PathVariable Integer feedbackId,
            @RequestBody AssessmentFeedbackDTO feedbackDTO);

    @DeleteMapping(path = "/assessments/feedback/{feedbackId}")
    ResponseEntity<Void> deleteFeedback(@PathVariable Integer feedbackId);
}
