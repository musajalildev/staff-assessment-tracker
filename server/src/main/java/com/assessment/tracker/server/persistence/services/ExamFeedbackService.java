package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExamFeedbackService {
    private final ExamFeedbackRepo feedbackRepository;
    private final AssessmentRepo assessmentRepository;
    private final UserRepository userRepository;
    private final ExamFeedbackMapper feedbackMapper;

    @Autowired
    public ExamFeedbackService(
            ExamFeedbackRepo feedbackRepository,
            AssessmentRepo assessmentRepository,
            UserRepository userRepository,
            ExamFeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
        this.feedbackMapper = feedbackMapper;
    }

    public List<ExamFeedbackDTO> getFeedbackByAssessmentID(Integer assessmentID) {
        List<ExamFeedback> feedbackList = feedbackRepository.findByAssessment_ID(assessmentID);
        return feedbackList.stream()
                .map(feedbackMapper::entityToApi)
                .collect(Collectors.toList());
    }

    public ExamFeedbackDTO getFeedbackByID(Integer feedbackID) {
        ExamFeedback feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return feedbackMapper.entityToApi(feedback);
    }

    public ExamFeedbackDTO createFeedback(Integer assessmentID, String feedbackText, UUID authorID) {
        Assessment assessment = assessmentRepository.findById(assessmentID)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        User author = userRepository.findById(authorID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExamFeedback feedback = new ExamFeedback();
        feedback.setAssessment(assessment);
        feedback.setAuthor(author);
        feedback.setFeedback(feedbackText);

        ExamFeedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.entityToApi(savedFeedback);
    }

    public ExamFeedbackDTO updateFeedback(Integer feedbackID, String feedbackText) {
        ExamFeedback feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setFeedback(feedbackText);
        ExamFeedback updatedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.entityToApi(updatedFeedback);
    }

    public void deleteFeedback(Integer feedbackID) {
        if (!feedbackRepository.existsById(feedbackID)) {
            throw new RuntimeException("Feedback not found");
        }
        feedbackRepository.deleteById(feedbackID);
    }
}
