package com.assessment.tracker.server.unit.entities;

import com.assessment.tracker.server.configuration.RsaKeyProperties;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.utils.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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

    @MockitoBean
    private RsaKeyProperties rsaKeyProperties;

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
                UserType.ACADEMIC
        );
    }
   @Override
    protected Object getId(User entity) {
        return entity.getUserID();
    }
}
