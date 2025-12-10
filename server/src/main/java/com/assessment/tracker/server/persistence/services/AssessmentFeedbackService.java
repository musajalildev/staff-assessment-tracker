package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.app.mappers.AssessmentFeedbackMapper;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssessmentFeedbackService {
    private final AssessmentFeedbackRepo feedbackRepository;
    private final AssessmentRepo assessmentRepository;
    private final UserRepository userRepository;
    private final AssessmentFeedbackMapper feedbackMapper;

    @Autowired
    public AssessmentFeedbackService(
            AssessmentFeedbackRepo feedbackRepository,
            AssessmentRepo assessmentRepository,
            UserRepository userRepository,
            AssessmentFeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
        this.feedbackMapper = feedbackMapper;
    }

    public List<AssessmentFeedbackDTO> getFeedbackByAssessmentID(Integer assessmentID) {
        List<AssessmentFeedback> feedbackList = feedbackRepository.findByAssessment_ID(assessmentID);
        return feedbackList.stream()
                .map(feedbackMapper::entityToApi)
                .collect(Collectors.toList());
    }

    public AssessmentFeedbackDTO getFeedbackByID(Integer feedbackID) {
        AssessmentFeedback feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return feedbackMapper.entityToApi(feedback);
    }

    public AssessmentFeedbackDTO createFeedback(Integer assessmentID, String feedbackText, UUID authorID) {
        Assessment assessment = assessmentRepository.findById(assessmentID)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        
        User author = userRepository.findById(authorID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AssessmentFeedback feedback = new AssessmentFeedback();
        feedback.setAssessment(assessment);
        feedback.setAuthor(author);
        feedback.setFeedback(feedbackText);

        AssessmentFeedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.entityToApi(savedFeedback);
    }

    public AssessmentFeedbackDTO updateFeedback(Integer feedbackID, String feedbackText) {
        AssessmentFeedback feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        
        feedback.setFeedback(feedbackText);
        AssessmentFeedback updatedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.entityToApi(updatedFeedback);
    }

    public void deleteFeedback(Integer feedbackID) {
        if (!feedbackRepository.existsById(feedbackID)) {
            throw new RuntimeException("Feedback not found");
        }
        feedbackRepository.deleteById(feedbackID);
    }
}

