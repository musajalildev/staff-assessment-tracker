package com.assessment.tracker.server.unit.services;

import com.assessment.tracker.server.api.dto.AssessmentDTO;
import com.assessment.tracker.server.app.mappers.AssessmentMapper;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.AssignedUser;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.AssessmentRepo;
import com.assessment.tracker.server.persistence.services.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @Mock
    private AssessmentRepo assessmentRepository;

    @Mock
    private AssessmentMapper assessmentMapper;

    @InjectMocks
    private AssessmentService assessmentService;

    private Assessment testAssessment;
    private AssessmentDTO testAssessmentDTO;

    @BeforeEach
    void setUp() {
        testAssessment = new Assessment();
        testAssessmentDTO = new AssessmentDTO();
    }

    @Nested
    @DisplayName("Save Assessment Tests")
    class SaveAssessmentTests {

        @Test
        @DisplayName("Should save assessment successfully")
        void save_Success() {
            when(assessmentRepository.save(testAssessment)).thenReturn(testAssessment);

            assessmentService.save(testAssessment);

            verify(assessmentRepository).save(testAssessment);
        }
    }

    @Nested
    @DisplayName("Get Assessment By ID Tests")
    class GetAssessmentByIdTests {

        @Test
        @DisplayName("Should return assessment DTO when found by ID")
        void getAssessmentByID_Success() {
            Integer assessmentId = 1;
            when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(testAssessment));
            when(assessmentMapper.entityToApi(testAssessment)).thenReturn(testAssessmentDTO);

            AssessmentDTO result = assessmentService.getAssessmentByID(assessmentId);

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testAssessmentDTO);
            verify(assessmentRepository).findById(assessmentId);
            verify(assessmentMapper).entityToApi(testAssessment);
        }
    }

    @Nested
    @DisplayName("Get All Assessments Tests")
    class GetAllAssessmentsTests {

        @Test
        @DisplayName("Should return all assessments as DTOs")
        void getAllAssessments_Success() {
            Assessment assessment1 = new Assessment();
            Assessment assessment2 = new Assessment();
            List<Assessment> assessments = Arrays.asList(assessment1, assessment2);

            AssessmentDTO dto1 = new AssessmentDTO();
            AssessmentDTO dto2 = new AssessmentDTO();

            when(assessmentRepository.findAll()).thenReturn(assessments);
            when(assessmentMapper.entityToApi(assessment1)).thenReturn(dto1);
            when(assessmentMapper.entityToApi(assessment2)).thenReturn(dto2);

            List<AssessmentDTO> result = assessmentService.getAllAssessments();

            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(dto1, dto2);
            verify(assessmentRepository).findAll();
            verify(assessmentMapper, times(2)).entityToApi(any(Assessment.class));
        }

        @Test
        @DisplayName("Should return empty list when no assessments exist")
        void getAllAssessments_EmptyList_Success() {
            when(assessmentRepository.findAll()).thenReturn(Collections.emptyList());

            List<AssessmentDTO> result = assessmentService.getAllAssessments();

            assertThat(result).isEmpty();
            verify(assessmentRepository).findAll();
            verify(assessmentMapper, never()).entityToApi(any(Assessment.class));
        }
    }

    @Nested
    @DisplayName("Get Involved Assessments Tests")
    class GetInvolvedAssessmentsTests {

        @Test
        @DisplayName("Should return assessments user is involved in")
        void getInvolvedAssessments_Success() {
            User user = mock(User.class);
            Assessment assessment1 = new Assessment();
            Assessment assessment2 = new Assessment();

            AssignedUser assignedUser1 = mock(AssignedUser.class);
            AssignedUser assignedUser2 = mock(AssignedUser.class);

            AssessmentDTO dto1 = new AssessmentDTO();
            AssessmentDTO dto2 = new AssessmentDTO();

            when(user.getAssignedUsers()).thenReturn(Arrays.asList(assignedUser1, assignedUser2));
            when(assignedUser1.getAssessment()).thenReturn(assessment1);
            when(assignedUser2.getAssessment()).thenReturn(assessment2);
            when(assessmentMapper.entityToApi(assessment1)).thenReturn(dto1);
            when(assessmentMapper.entityToApi(assessment2)).thenReturn(dto2);

            List<AssessmentDTO> result = assessmentService.getInvolvedAssessments(user);

            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(dto1, dto2);
            verify(user).getAssignedUsers();
            verify(assessmentMapper, times(2)).entityToApi(any(Assessment.class));
        }

        @Test
        @DisplayName("Should return empty list when user has no assignments")
        void getInvolvedAssessments_NoAssignments_Success() {
            User user = mock(User.class);
            when(user.getAssignedUsers()).thenReturn(Collections.emptyList());

            List<AssessmentDTO> result = assessmentService.getInvolvedAssessments(user);

            assertThat(result).isEmpty();
            verify(user).getAssignedUsers();
            verify(assessmentMapper, never()).entityToApi(any(Assessment.class));
        }
    }
}
