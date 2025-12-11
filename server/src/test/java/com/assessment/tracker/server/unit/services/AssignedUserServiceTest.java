package com.assessment.tracker.server.unit.services;

import com.assessment.tracker.server.api.dto.AssessmentRolesDTO;
import com.assessment.tracker.server.app.mappers.AssessmentRoleMapper;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.AssignedUser;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.AssignedUserRepository;
import com.assessment.tracker.server.persistence.repos.UserRepository;
import com.assessment.tracker.server.persistence.services.AssignedUserService;
import com.assessment.tracker.server.persistence.services.UserService;
import com.assessment.tracker.server.utils.enums.AssessmentRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignedUserServiceTest {

    @Mock
    private AssignedUserRepository assignedUserRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssessmentRoleMapper assessmentRoleMapper;

    @InjectMocks
    private AssignedUserService assignedUserService;

    private User testUser;
    private UUID testUserId;
    private Assessment testAssessment;
    private AssignedUser testAssignedUser;
    private AssessmentRole testRole;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new User();
        testAssessment = new Assessment();
        testRole = AssessmentRole.ROLE_CHECKER;
        testAssignedUser = new AssignedUser(testUser, testRole, testAssessment);
    }

    @Nested
    class CreateAssignmentTests {

        @Test
        void createAssignment_WithUUID_Success() {
            when(userService.getUser(testUserId)).thenReturn(testUser);
            when(assignedUserRepository.existsByUserAndRole(testUser, testRole)).thenReturn(false);
            when(assignedUserRepository.save(any(AssignedUser.class))).thenReturn(testAssignedUser);

            AssignedUser result = assignedUserService.createAssignment(testUserId, testRole, testAssessment);

            assertThat(result).isNotNull();
            assertThat(result.getRole()).isEqualTo(testRole);
            verify(userService).getUser(testUserId);
            verify(assignedUserRepository).existsByUserAndRole(testUser, testRole);
            verify(assignedUserRepository).save(any(AssignedUser.class));
        }

        @Test
        void createAssignment_WithUsername_Success() {
            String username = "testuser";
            when(userService.getUserByEmail(username)).thenReturn(null);
            when(userService.getUserByUsername(username)).thenReturn(testUser);
            when(assignedUserRepository.existsByUserAndRole(testUser, testRole)).thenReturn(false);
            when(assignedUserRepository.save(any(AssignedUser.class))).thenReturn(testAssignedUser);

            AssignedUser result = assignedUserService.createAssignment(username, testRole, testAssessment);

            assertThat(result).isNotNull();
            assertThat(result.getRole()).isEqualTo(testRole);
            verify(assignedUserRepository).save(any(AssignedUser.class));
        }

        @Test
        void createAssignment_WithEmail_Success() {
            String email = "test@example.com";
            when(userService.getUserByEmail(email)).thenReturn(testUser);
            when(assignedUserRepository.existsByUserAndRole(testUser, testRole)).thenReturn(false);
            when(assignedUserRepository.save(any(AssignedUser.class))).thenReturn(testAssignedUser);

            AssignedUser result = assignedUserService.createAssignment(email, testRole, testAssessment);

            assertThat(result).isNotNull();
            verify(userService).getUserByEmail(email);
            verify(assignedUserRepository).save(any(AssignedUser.class));
        }
    }

    @Nested
    class ReadAssignmentTests {

        @Test
        void getAllAssignedUsers_Success() {
            AssignedUser assignedUser1 = new AssignedUser(testUser, testRole, testAssessment);
            AssignedUser assignedUser2 = new AssignedUser(testUser, AssessmentRole.ROLE_INVOLVED, testAssessment);
            List<AssignedUser> assignedUsers = Arrays.asList(assignedUser1, assignedUser2);

            AssessmentRolesDTO dto1 = new AssessmentRolesDTO();
            AssessmentRolesDTO dto2 = new AssessmentRolesDTO();

            when(assignedUserRepository.findAll()).thenReturn(assignedUsers);
            when(assessmentRoleMapper.entityToApi(assignedUser1)).thenReturn(dto1);
            when(assessmentRoleMapper.entityToApi(assignedUser2)).thenReturn(dto2);

            List<AssessmentRolesDTO> result = assignedUserService.getAllAssignedUsers();

            assertThat(result).hasSize(2);
            verify(assignedUserRepository).findAll();
            verify(assessmentRoleMapper, times(2)).entityToApi(any(AssignedUser.class));
        }

        @Test
        void getAssignedUser_ById_Success() {
            int assignmentId = 1;
            when(assignedUserRepository.findById(assignmentId)).thenReturn(Optional.of(testAssignedUser));

            AssignedUser result = assignedUserService.getAssignedUser(assignmentId);

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testAssignedUser);
            verify(assignedUserRepository).findById(assignmentId);
        }

        @Test
        void getAssignedUser_ByUsername_Success() {
            String username = "testuser";
            AssignedUser assignedUser1 = new AssignedUser(testUser, testRole, testAssessment);
            AssignedUser assignedUser2 = new AssignedUser(testUser, AssessmentRole.ROLE_INVOLVED, testAssessment);

            when(userService.getUserByEmail(username)).thenReturn(null);
            when(userService.getUserByUsername(username)).thenReturn(testUser);
            when(assignedUserRepository.findAll()).thenReturn(Arrays.asList(assignedUser1, assignedUser2));

            List<AssignedUser> result = assignedUserService.getAssignedUser(username);

            assertThat(result).hasSize(2);
            verify(assignedUserRepository).findAll();
        }

        @Test
        void getUserAssignment_Success() {
            AssignedUser assignedUser1 = new AssignedUser(testUser, testRole, testAssessment);
            AssignedUser assignedUser2 = new AssignedUser(testUser, AssessmentRole.ROLE_INVOLVED, testAssessment);

            when(userRepository.findByUserID(testUserId)).thenReturn(testUser);
            when(assignedUserRepository.findAllByUser(testUser)).thenReturn(Arrays.asList(assignedUser1, assignedUser2));

            List<AssessmentRole> result = assignedUserService.getUserAssignment(testUserId);

            assertThat(result).hasSize(2);
            assertThat(result).contains(testRole, AssessmentRole.ROLE_INVOLVED);
            verify(userRepository).findByUserID(testUserId);
            verify(assignedUserRepository).findAllByUser(testUser);
        }

        @Test
        void getAllCommonRole_Success() {
            AssignedUser assignedUser1 = new AssignedUser(testUser, testRole, testAssessment);
            AssignedUser assignedUser2 = new AssignedUser(new User(), testRole, testAssessment);
            List<AssignedUser> usersWithRole = Arrays.asList(assignedUser1, assignedUser2);

            when(assignedUserRepository.findAllByRole(testRole)).thenReturn(usersWithRole);

            List<AssignedUser> result = assignedUserService.getAllCommonRole(testRole);

            assertThat(result).hasSize(2);
            verify(assignedUserRepository).findAllByRole(testRole);
        }
    }

    @Nested
    class DeleteAssignmentTests {

        @Test
        void deleteAssignedUser_Success() {
            int assignmentId = 1;
            when(assignedUserRepository.existsById(assignmentId)).thenReturn(true);
            doNothing().when(assignedUserRepository).deleteById(assignmentId);

            boolean result = assignedUserService.deleteAssignedUser(assignmentId);

            assertThat(result).isTrue();
            verify(assignedUserRepository).existsById(assignmentId);
            verify(assignedUserRepository).deleteById(assignmentId);
        }

        @Test
        void deleteAllUserAssignments_Success() {
            List<AssignedUser> assignments = Arrays.asList(testAssignedUser, new AssignedUser());

            when(userService.getUser(testUserId)).thenReturn(testUser);
            when(assignedUserRepository.findAllByUser(testUser)).thenReturn(assignments);
            doNothing().when(assignedUserRepository).deleteAll(assignments);

            boolean result = assignedUserService.deleteAllUserAssignments(testUserId);

            assertThat(result).isTrue();
            verify(userService).getUser(testUserId);
            verify(assignedUserRepository).findAllByUser(testUser);
            verify(assignedUserRepository).deleteAll(assignments);
        }

        @Test
        void deleteAllAssignments_Success() {
            doNothing().when(assignedUserRepository).deleteAll();

            boolean result = assignedUserService.deleteAllAssignments();

            assertThat(result).isTrue();
            verify(assignedUserRepository).deleteAll();
        }
    }

    @Nested
    class UpdateAssignmentTests {

        @Test
        void updateUserAssignment_ById_Success() {
            int assignmentId = 1;
            AssessmentRole newRole = AssessmentRole.ROLE_INVOLVED;
            AssignedUser updatedAssignment = new AssignedUser(testUser, newRole, testAssessment);

            when(assignedUserRepository.findById(assignmentId)).thenReturn(Optional.of(testAssignedUser));
            when(assignedUserRepository.save(any(AssignedUser.class))).thenReturn(updatedAssignment);

            AssignedUser result = assignedUserService.updateUserAssignment(newRole, assignmentId);

            assertThat(result).isNotNull();
            assertThat(result.getRole()).isEqualTo(newRole);
            verify(assignedUserRepository).findById(assignmentId);
            verify(assignedUserRepository).save(any(AssignedUser.class));
        }

        @Test
        void updateUserAssignment_ByUsername_Success() {
            String username = "testuser";
            AssessmentRole newRole = AssessmentRole.ROLE_INVOLVED;
            AssignedUser currentAssignment = new AssignedUser(testUser, testRole, testAssessment);
            AssignedUser updatedAssignment = new AssignedUser(testUser, newRole, testAssessment);

            when(userService.getUserByEmail(username)).thenReturn(null);
            when(userService.getUserByUsername(username)).thenReturn(testUser);
            when(assignedUserRepository.findByUser(testUser)).thenReturn(currentAssignment);
            when(assignedUserRepository.save(any(AssignedUser.class))).thenReturn(updatedAssignment);

            AssignedUser result = assignedUserService.updateUserAssignment(newRole, username);

            assertThat(result).isNotNull();
            verify(assignedUserRepository).findByUser(testUser);
            verify(assignedUserRepository).save(any(AssignedUser.class));
        }
    }

    @Nested
    class HelperMethodTests {

        @Test
        void findUserWithString_ByEmail_Success() {
            String email = "test@example.com";
            when(userService.getUserByEmail(email)).thenReturn(testUser);

            User result = assignedUserService.findUserWithString(email);

            assertThat(result).isEqualTo(testUser);
            verify(userService).getUserByEmail(email);
            verify(userService, never()).getUserByUsername(anyString());
        }

        @Test
        void findUserWithString_ByUsername_Success() {
            String username = "testuser";
            when(userService.getUserByEmail(username)).thenReturn(null);
            when(userService.getUserByUsername(username)).thenReturn(testUser);

            User result = assignedUserService.findUserWithString(username);

            assertThat(result).isEqualTo(testUser);
            verify(userService).getUserByEmail(username);
            verify(userService).getUserByUsername(username);
        }
    }
}
