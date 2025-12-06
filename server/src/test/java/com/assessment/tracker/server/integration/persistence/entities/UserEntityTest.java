package com.assessment.tracker.server.integration.persistence.entities;

import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.utils.enums.userType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserEntityTest extends EntityTest<User> {

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    void testUpdate() {
        entityManager.persist(entity);
        entityManager.flush();

        User user = entityManager.find(User.class, getId(entity));
        assertNotNull(user);

        user.setUsername("newusername");
        entityManager.flush();

        User updatedUser = entityManager.find(User.class, user.getUserID());
        assertNotNull(updatedUser);
        assertEquals("newusername", updatedUser.getUsername());
    }

    @Override
    protected User createEntity() {
        return new User(
                "username",
                "email@email.com",
                "superSecretPassword",
                userType.ACADEMIC
        );
    }
   @Override
    protected Object getId(User entity) {
        return entity.getUserID();
    }
}
