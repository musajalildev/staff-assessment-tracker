package com.assessment.tracker.server.unit.repositories;

import com.assessment.tracker.server.configuration.RsaKeyProperties;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.UserRepository;
import com.assessment.tracker.server.utils.enums.UserType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.*;

@DataJpaTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @MockitoBean
    private RsaKeyProperties rsaKeyProperties;

    void createEntity() {
        user = new User(
                "user",
                "email@email.com",
                "password",
                UserType.ROLE_ACADEMIC);

        entityManager.persistAndFlush(user);
    }
    void removeEntity() {
        User entity = entityManager.find(User.class, user.getUserID());

        if (Objects.isNull(entity) ) {
            return;
        }

        entityManager.remove(entity);
    }

    @BeforeEach
    void setup() {
        createEntity();
    }

    @AfterEach
    void teardown() {
        removeEntity();
    }

    @Test
    public void testFindByUsername() {
        User entity = userRepository.findByUsername("user");

        assertNotNull("Null User", entity);
        assertEquals("Failed to get user invalid user", entity.getUsername(), "user");
    }

    @Test
    public void testFindByEmail() {
        User entity = userRepository.findByEmail("email@email.com");
        assertNotNull("Null User", entity);
        assertEquals("Failed to get user invalid user", entity.getEmail(), "email@email.com");
    }

    @Test
    public void testFindByUserId() {
        User entity = userRepository.findByUserID(user.getUserID());
        assertNotNull("Null User", entity);
        assertEquals("Failed to get user invalid user", entity.getUsername(), "user");
    }

    @Test
    public void testExistsByUserId() {
        boolean entity = userRepository.existsByUserID(user.getUserID());
        assertTrue(entity, "User does not exist");
    }

    @Test
    public void testDeleteByUserId() {
        userRepository.deleteByUserID(user.getUserID());
        boolean exists = userRepository.existsByUserID(user.getUserID());
        assertFalse("User not deleted", exists);
    }

    @Test
    public void testFindAllByUserType() {
        List<User> users = userRepository.findAllByUserType(UserType.ROLE_ACADEMIC);
        assertNotNull("Null Users", users);
    }
}
