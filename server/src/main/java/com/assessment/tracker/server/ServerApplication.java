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
            AssessmentRepo assessmentRepository,
            LogRepository logRepository,
            UserLogRepository userLogRepository,
            AssessmentLogRepository assessmentLogRepository,
            ModuleRepo moduleRepo,
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

                AssignedUser a1 = new AssignedUser(john, AssesmentRole.ROLE_CHECKER, null);
                AssignedUser a2 = new AssignedUser(john, AssesmentRole.ROLE_SETTER, null);

                AssignedUser a3 = new AssignedUser(mary, AssesmentRole.ROLE_CHECKER, null);

                AssignedUser a4 = new AssignedUser(kofi, AssesmentRole.ROLE_SETTER, null);
                AssignedUser a5 = new AssignedUser(kofi, AssesmentRole.ROLE_EXAM_OFFICER, null);

                AssignedUser a6 = new AssignedUser(sakura, AssesmentRole.ROLE_CHECKER, null);

                AssignedUser a7 = new AssignedUser(musa, AssesmentRole.ROLE_SETTER, null);
                AssignedUser a8 = new AssignedUser(musa, AssesmentRole.ROLE_SETTER,null);

                assignedUserRepository.saveAll(
                        List.of(a1, a2, a3, a4, a5, a6, a7, a8));

                if (assignedUserRepository.count() == 0) {
                    System.out.println("Error w assigned user repo");
                }

                System.out.println("Assignments seeded.");
            }

            if (assessmentRepository.count() == 0) {
                Assessment a1 = new Assessment();
                a1.setTitle("test");
                a1.setProgress(AssessmentProgress.CHECKED);
                a1.setAssessmentType(AssessmentType.TEST_AUTOGRADED);

                assessmentRepository.saveAll(List.of(a1));

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
        User u1 = new User("john", "john@example.com", "john123", UserType.ROLE_ACADEMIC);
        User u2 = new User("mary", "mary@example.com", "mary123", UserType.ROLE_TEACHING_SUPPORT);
        User u3 = new User("kofi", "kofi@example.com", "kofi123", UserType.ROLE_EXTERNAL_EXAMINER);
        User u4 = new User("sakura", "sakura@example.com", "sakura123", UserType.ROLE_ACADEMIC);
        User u5 = new User("musa", "musa@example.com", "musa123", UserType.ROLE_TEACHING_SUPPORT);

        // password encoding for test data
        return List.of(u1, u2, u3, u4, u5);
    }
}
