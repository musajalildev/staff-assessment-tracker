package com.assessment.tracker.server.unit.services;

import com.assessment.tracker.server.api.dto.AssessmentFeedbackDTO;
import com.assessment.tracker.server.app.mappers.AssessmentFeedbackMapper;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.AssessmentFeedback;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.AssessmentFeedbackRepo;
import com.assessment.tracker.server.persistence.repos.AssessmentRepo;
import com.assessment.tracker.server.persistence.repos.UserRepository;
import com.assessment.tracker.server.persistence.services.AssessmentFeedbackService;
import com.assessment.tracker.server.utils.enums.FeedbackType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentFeedbackServiceTest {

    @Mock
    private AssessmentFeedbackRepo feedbackRepository;

    @Mock
    private AssessmentRepo assessmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssessmentFeedbackMapper feedbackMapper;

    @InjectMocks
    private AssessmentFeedbackService assessmentFeedbackService;

    private AssessmentFeedback testFeedback;
    private AssessmentFeedbackDTO testFeedbackDTO;
    private Assessment testAssessment;
    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testFeedback = new AssessmentFeedback();
        testFeedbackDTO = new AssessmentFeedbackDTO();
        testAssessment = new Assessment();
        testUser = new User();
        testUserId = UUID.randomUUID();
    }

    @Test
    void getFeedbackByAssessmentID_Success() {
        Integer assessmentId = 1;
        AssessmentFeedback feedback1 = new AssessmentFeedback();
        AssessmentFeedback feedback2 = new AssessmentFeedback();
        List<AssessmentFeedback> feedbackList = Arrays.asList(feedback1, feedback2);

        AssessmentFeedbackDTO dto1 = new AssessmentFeedbackDTO();
        AssessmentFeedbackDTO dto2 = new AssessmentFeedbackDTO();

        when(feedbackRepository.findByAssessment_ID(assessmentId)).thenReturn(feedbackList);
        when(feedbackMapper.entityToApi(feedback1)).thenReturn(dto1);
        when(feedbackMapper.entityToApi(feedback2)).thenReturn(dto2);

        List<AssessmentFeedbackDTO> result = assessmentFeedbackService.getFeedbackByAssessmentID(assessmentId);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);
        verify(feedbackRepository).findByAssessment_ID(assessmentId);
        verify(feedbackMapper, times(2)).entityToApi(any(AssessmentFeedback.class));
    }

    @Test
    void getFeedbackByAssessmentID_EmptyList_Success() {
        Integer assessmentId = 1;
        when(feedbackRepository.findByAssessment_ID(assessmentId)).thenReturn(Collections.emptyList());

        List<AssessmentFeedbackDTO> result = assessmentFeedbackService.getFeedbackByAssessmentID(assessmentId);

        assertThat(result).isEmpty();
        verify(feedbackRepository).findByAssessment_ID(assessmentId);
        verify(feedbackMapper, never()).entityToApi(any(AssessmentFeedback.class));
    }

    @Test
    void getFeedbackByID_Success() {
        Integer feedbackId = 1;
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(testFeedback));
        when(feedbackMapper.entityToApi(testFeedback)).thenReturn(testFeedbackDTO);

        AssessmentFeedbackDTO result = assessmentFeedbackService.getFeedbackByID(feedbackId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testFeedbackDTO);
        verify(feedbackRepository).findById(feedbackId);
        verify(feedbackMapper).entityToApi(testFeedback);
    }

    @Test
    void createFeedback_Success() {
        Integer assessmentId = 1;
        String feedbackText = "Great assessment!";
        FeedbackType feedbackType = FeedbackType.CHECKER;

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(testAssessment));
        when(userRepository.findByUserID(testUserId)).thenReturn(testUser);
        when(feedbackRepository.save(any(AssessmentFeedback.class))).thenReturn(testFeedback);
        when(feedbackMapper.entityToApi(testFeedback)).thenReturn(testFeedbackDTO);

        AssessmentFeedbackDTO result = assessmentFeedbackService.createFeedback(
                assessmentId, feedbackText, testUserId, feedbackType);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testFeedbackDTO);
        verify(assessmentRepository).findById(assessmentId);
        verify(userRepository).findByUserID(testUserId);
        verify(feedbackRepository).save(any(AssessmentFeedback.class));
        verify(feedbackMapper).entityToApi(testFeedback);
    }

    @Test
    void updateFeedback_Success() {
        Integer feedbackId = 1;
        String newFeedbackText = "Updated feedback text";
        AssessmentFeedback updatedFeedback = new AssessmentFeedback();
        AssessmentFeedbackDTO updatedDTO = new AssessmentFeedbackDTO();

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(testFeedback));
        when(feedbackRepository.save(testFeedback)).thenReturn(updatedFeedback);
        when(feedbackMapper.entityToApi(updatedFeedback)).thenReturn(updatedDTO);

        AssessmentFeedbackDTO result = assessmentFeedbackService.updateFeedback(feedbackId, newFeedbackText);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(updatedDTO);
        verify(feedbackRepository).findById(feedbackId);
        verify(feedbackRepository).save(testFeedback);
        verify(feedbackMapper).entityToApi(updatedFeedback);
    }

    @Test
    void deleteFeedback_Success() {
        Integer feedbackId = 1;
        when(feedbackRepository.existsById(feedbackId)).thenReturn(true);
        doNothing().when(feedbackRepository).deleteById(feedbackId);

        assessmentFeedbackService.deleteFeedback(feedbackId);

        verify(feedbackRepository).existsById(feedbackId);
        verify(feedbackRepository).deleteById(feedbackId);
    }
}