package com.assessment.tracker.server;

import com.assessment.tracker.server.persistence.assignedusers.AssignedUser;
import com.assessment.tracker.server.utils.Role;
import com.assessment.tracker.server.persistence.user.User;
import com.assessment.tracker.server.persistence.assignedusers.AssignedUserRepository;
import com.assessment.tracker.server.persistence.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository,
                                   AssignedUserRepository assignedUserRepository) {
        return args -> {

            // Seed Users only if empty
            if (userRepository.count() == 0) {
                User u1 = new User("john", "john@example.com", "john123");
                User u2 = new User("mary", "mary@example.com", "mary123");
                User u3 = new User("kofi", "kofi@example.com", "kofi123");
                User u4 = new User("sakura", "sakura@example.com", "sakura123");
                User u5 = new User("musa", "musa@example.com", "musa123");

                userRepository.saveAll(List.of(u1, u2, u3, u4, u5));

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
                AssignedUser a2 = new AssignedUser(john, Role.EXAMS_OFFICER);

                AssignedUser a3 = new AssignedUser(mary, Role.TEACHING_SUPPORT);

                AssignedUser a4 = new AssignedUser(kofi, Role.EXTERNAL_EXAMINER);
                AssignedUser a5 = new AssignedUser(kofi, Role.ACADEMIC);

                AssignedUser a6 = new AssignedUser(sakura, Role.TEACHING_SUPPORT);

                AssignedUser a7 = new AssignedUser(musa, Role.EXAMS_OFFICER);
                AssignedUser a8 = new AssignedUser(musa, Role.ACADEMIC);

                assignedUserRepository.saveAll(
                        List.of(a1, a2, a3, a4, a5, a6, a7, a8)
                );

                System.out.println("Assignments seeded.");
            }


        };
    }}

