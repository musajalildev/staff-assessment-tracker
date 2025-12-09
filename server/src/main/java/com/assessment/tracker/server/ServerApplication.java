package com.assessment.tracker.server;

import com.assessment.tracker.server.configuration.RsaKeyProperties;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.repos.logging.*;
import com.assessment.tracker.server.persistence.services.*;
import com.assessment.tracker.server.persistence.services.logging.*;

import com.assessment.tracker.server.utils.enums.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository,
            AssignedUserRepository assignedUserRepository,
            PasswordEncoder pcoder,
            ModuleRepo moduleRepository,
            AssessmentRepo assessmentRepository,
            LogRepository logRepository,
            UserLogRepository userLogRepository,
            AssessmentLogRepository assessmentLogRepository,
            ModuleLogRepository moduleLogRepository) {

        return args -> {

            // Seed Users only if empty
            if (userRepository.count() == 0) {
                List<User> users = getUsers();
                if (users.isEmpty()) {
                    System.out.println("No users to seed.");
                }
                for (User user : users) {
                    user.setPassword(pcoder.encode(user.getPassword()));
                }

                userRepository.saveAll(users);

                System.out.println("Users seeded.");
            }

            // Seed AssignedUser only if empty
            if (assignedUserRepository.count() == 0) {

                User john = userRepository.findByUsername("john");
                User mary = userRepository.findByUsername("mary");
                User kofi = userRepository.findByUsername("kofi");
                User sakura = userRepository.findByUsername("sakura");
                User musa = userRepository.findByUsername("musa");

                AssignedUser a1 = new AssignedUser(john, Role.ACADEMIC);
                AssignedUser a2 = new AssignedUser(john, Role.TEACHING_SUPPORT);

                AssignedUser a3 = new AssignedUser(mary, Role.EXAM_OFFICER);

                AssignedUser a4 = new AssignedUser(kofi, Role.EXTERNAL_EXAMINER);
                AssignedUser a5 = new AssignedUser(kofi, Role.ACADEMIC);

                AssignedUser a6 = new AssignedUser(sakura, Role.TEACHING_SUPPORT);

                AssignedUser a7 = new AssignedUser(musa, Role.EXAM_OFFICER);
                AssignedUser a8 = new AssignedUser(musa, Role.EXTERNAL_EXAMINER);

                assignedUserRepository.saveAll(
                        List.of(a1, a2, a3, a4, a5, a6, a7, a8));

                System.out.println("Assignments seeded.");
            }
            if (moduleRepository.count() == 0) {
                Module a1 = new Module();
                a1.setTitle("test");
                a1.setCode(3);
                moduleRepository.saveAll(List.of(a1));

                System.out.println("Modules seeded.");
            }
            if (assessmentRepository.count() == 0) {
                Assessment a1 = new Assessment();
                a1.setTitle("Test Autograded");
                a1.setProgress(AssessmentProgress.CHECKED);
                a1.setAssessmentType(AssessmentType.TEST_AUTOGRADED);
                a1.setModule(moduleRepository.findByCode(3));
                assessmentRepository.saveAll(List.of(a1));

                Assessment a2 = new Assessment();
                a2.setTitle("Test Team Marker");
                a2.setProgress(AssessmentProgress.CREATED);
                a2.setAssessmentType(AssessmentType.TEST_TEAM_MARKER);
                a2.setModule(moduleRepository.findByCode(3));

                Assessment a3 = new Assessment();
                a3.setTitle("Test Single Marker");
                a3.setProgress(AssessmentProgress.CREATED);
                a3.setAssessmentType(AssessmentType.TEST_SINGLE_MARKER);
                a3.setModule(moduleRepository.findByCode(3));

                Assessment a4 = new Assessment();
                a4.setTitle("Exam");
                a4.setProgress(AssessmentProgress.CREATED);
                a4.setAssessmentType(AssessmentType.EXAM);
                a4.setModule(moduleRepository.findByCode(3));

                Assessment a5 = new Assessment();
                a5.setTitle("Coursework");
                a5.setProgress(AssessmentProgress.CREATED);
                a5.setAssessmentType(AssessmentType.COURSEWORK);
                a5.setModule(moduleRepository.findByCode(3));

                assessmentRepository.saveAll(List.of(a1, a2, a3, a4, a5));
                System.out.println("Assessments seeded.");
            }
            if (logRepository.count() == 0) {
                Log l1 = new Log();
                l1.setUser(userRepository.findByUsername("john"));
                l1.setComment("Test");
                logRepository.saveAll(List.of(l1));

                System.out.println("Base Log seeded.");
            }
            if (userLogRepository.count() == 0) {
                UserLog l1 = new UserLog();
                l1.setUser(userRepository.findByUsername("mary"));
                l1.setComment("User Test");
                l1.setActionType(UserActions.DELETE);
                l1.setTargetUser(userRepository.findByUsername("john"));
                userLogRepository.saveAll(List.of(l1));

                System.out.println("User Log seeded.");
            }
            if (assessmentLogRepository.count() == 0) {
                AssessmentLog l1 = new AssessmentLog();
                l1.setUser(userRepository.findByUsername("kofi"));
                l1.setComment("Assessment Test");
                l1.setTargetAssessment(assessmentRepository.findByID(1));
                l1.setActionType(AssessmentActions.PROGRESS);
                l1.setPreviousState(AssessmentProgress.CHECKED);
                l1.setNewState(AssessmentProgress.COMPLETE);
                assessmentLogRepository.saveAll(List.of(l1));

                System.out.println("Assessment Log seeded.");
            }

        };
    }

    private static List<User> getUsers() {
        User u1 = new User("john", "john@example.com", "john123", UserType.ACADEMIC);
        User u2 = new User("mary", "mary@example.com", "mary123", UserType.TEACHING_SUPPORT);
        User u3 = new User("kofi", "kofi@example.com", "kofi123", UserType.EXTERNAL_EXAMINER);
        User u4 = new User("sakura", "sakura@example.com", "sakura123", UserType.ACADEMIC);
        User u5 = new User("musa", "musa@example.com", "musa123", UserType.ACADEMIC);

        // password encoding for test data
        return List.of(u1, u2, u3, u4, u5);
    }
}
