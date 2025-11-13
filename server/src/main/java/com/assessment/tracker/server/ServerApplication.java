package com.assessment.tracker.server;

import com.assessment.tracker.server.persistence.domain.User;
import com.assessment.tracker.server.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            userService.createUser(new User("user1","boo","a"));
            userService.createUser(new User("user2","faa",null));
            userService.createUser(new User("user3","foo","b"));


        };
    }

}
