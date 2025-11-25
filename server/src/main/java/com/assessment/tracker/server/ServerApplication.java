package com.assessment.tracker.server;

import com.assessment.tracker.server.persistence.domain.AssignedUser;
import com.assessment.tracker.server.persistence.domain.Role;
import com.assessment.tracker.server.persistence.domain.User;
import com.assessment.tracker.server.persistence.repository.AssignedUserRepository;
import com.assessment.tracker.server.persistence.repository.UserRepository;
import com.assessment.tracker.server.services.UserService;
import com.assessment.tracker.server.utils.AssessmentProgress;
import com.assessment.tracker.server.utils.AssessmentType;
import com.assessment.tracker.server.api.assessment.*;
import com.assessment.tracker.server.persistence.assignedusers.AssignedUser;
import com.assessment.tracker.server.persistence.user.*;
import com.assessment.tracker.server.persistence.user.User.userType;
import com.assessment.tracker.server.utils.Role;
import com.assessment.tracker.server.persistence.assignedusers.AssignedUserRepository;
import com.assessment.tracker.server.persistence.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository,
                                   AssignedUserRepository assignedUserRepository,
                                   PasswordEncoder pcoder) {


            AssignedUserRepository assignedUserRepository, AssessmentRepo assessmentRepository) {
        return args -> {

            // Seed Users only if empty
            if (userRepository.count() == 0) {
                List<User> users = getUsers();
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

                AssignedUser a1 = new AssignedUser(john, Role.CHECKER);
                AssignedUser a2 = new AssignedUser(john, Role.SETTER);

                AssignedUser a3 = new AssignedUser(mary, Role.MODULE_STAFF);

                AssignedUser a4 = new AssignedUser(kofi, Role.MODULE_LEAD);
                AssignedUser a5 = new AssignedUser(kofi, Role.EXAM_OFFICER);

                AssignedUser a6 = new AssignedUser(sakura, Role.MODULE_STAFF);

                AssignedUser a7 = new AssignedUser(musa, Role.SETTER);
                AssignedUser a8 = new AssignedUser(musa, Role.MODULE_MODERATOR);

                assignedUserRepository.saveAll(
                        List.of(a1, a2, a3, a4, a5, a6, a7, a8)
                );

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

        };
    }

    private static List<User> getUsers() {
        User u1 = new User("john", "john@example.com", "john123", userType.EXAMS_OFFICER);
        User u2 = new User("mary", "mary@example.com", "mary123", userType.TEACHING_SUPPORT);
        User u3 = new User("kofi", "kofi@example.com", "kofi123", userType.EXTERNAL_EXAMINER);
        User u4 = new User("sakura", "sakura@example.com", "sakura123", userType.ACADEMIC);
        User u5 = new User("musa", "musa@example.com", "musa123", userType.ACADEMIC);

        //password encoding for test data
        return List.of(u1, u2, u3, u4, u5);
    }
}

