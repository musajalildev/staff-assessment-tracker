package com.assessment.tracker.server.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.dto.*;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.services.*;

import java.util.List;

@RestController
public class AssessmentFeedbackControllerImpl implements AssessmentFeedbackController {

    private final AssessmentFeedbackService feedbackService;
    private final UserService userService;

    @Autowired
    public AssessmentFeedbackControllerImpl(
            AssessmentFeedbackService feedbackService,
            UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<AssessmentFeedbackDTO>> getFeedbackByAssessment(Integer assessmentId) {
        try {
            List<AssessmentFeedbackDTO> feedbackList = feedbackService.getFeedbackByAssessmentID(assessmentId);
            return ResponseEntity.ok(feedbackList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<AssessmentFeedbackDTO> getFeedbackById(Integer feedbackId) {
        try {
            AssessmentFeedbackDTO feedback = feedbackService.getFeedbackByID(feedbackId);
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<AssessmentFeedbackDTO> createFeedback(
            Integer assessmentId,
            AssessmentFeedbackDTO feedbackDTO) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Get username from authentication
            String username = authentication.getName();
            User author = userService.getUserByUsername(username);
            if (author == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Create feedback
            AssessmentFeedbackDTO createdFeedback = feedbackService.createFeedback(
                    assessmentId,
                    feedbackDTO.getFeedback(),
                    author.getUserID());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<AssessmentFeedbackDTO> updateFeedback(
            Integer feedbackId,
            AssessmentFeedbackDTO feedbackDTO) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Verify the user owns this feedback (optional - can be enhanced with proper
            // authorization)
            AssessmentFeedbackDTO existingFeedback = feedbackService.getFeedbackByID(feedbackId);
            if (existingFeedback == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Update feedback
            AssessmentFeedbackDTO updatedFeedback = feedbackService.updateFeedback(
                    feedbackId,
                    feedbackDTO.getFeedback());

            return ResponseEntity.ok(updatedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteFeedback(Integer feedbackId) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Verify the user owns this feedback (optional - can be enhanced with proper
            // authorization)
            AssessmentFeedbackDTO existingFeedback = feedbackService.getFeedbackByID(feedbackId);
            if (existingFeedback == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            feedbackService.deleteFeedback(feedbackId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
