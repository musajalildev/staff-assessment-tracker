package com.assessment.tracker.server.unit.persistence.repositories;

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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.*;

@DataJpaTest
@Transactional
class UserRepositoryTest {

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

    @BeforeEach
    void setup() {
        user = new User(
                "user",
                "email@email.com",
                "password",
                UserType.ROLE_ACADEMIC);

        entityManager.persistAndFlush(user);
    }

    @AfterEach
    void teardown() {
        User entity = entityManager.find(User.class, user.getUserID());
        if (entity == null) {
            return;
        }
        entityManager.remove(entity);
    }

    // Selecting Tests
    @Test
    public void findByUsername_UsernameDoesNotExist_ReturnsNull() {
        User entity = userRepository.findByUsername("InvalidUsername");
        assertNull("User is not null", entity);
    }

    @Test
    public void findByUsername_UsernameExists_ReturnsUser() {
        User entity = userRepository.findByUsername("user");

        assertNotNull("Null User", entity);
        assertEquals("Failed to get user invalid user", entity.getUsername(), "user");
    }

    @Test
    public void findByEmail_EmailDoesNotExist_ReturnsNull() {
        User entity = userRepository.findByEmail("InvalidEmail");
        assertNull("User is not null", entity);
    }

    @Test
    public void findByEmail_EmailExists_ReturnsUser() {
        User entity = userRepository.findByEmail("email@email.com");
        assertNotNull("Null User", entity);
        assertEquals("Failed to get user invalid user", entity.getEmail(), "email@email.com");
    }

    @Test
    public void findByUserId_UserIdExists_ReturnsUser() {
        User entity = userRepository.findByUserID(user.getUserID());
        assertNotNull("Null User", entity);
        assertEquals("Failed to get user invalid user", entity.getUsername(), "user");
    }

    // Exists Tests
    @Test
    public void existsByEmail_UserExists_ReturnsTrue(){
        boolean exists = userRepository.existsByEmail(user.getEmail());
        assertTrue(exists);
    }

    @Test
    public void existsByEmail_UserDoesNotExist_ReturnsFalse(){
        boolean exists = userRepository.existsByEmail("InvalidEmail");
        assertFalse("User exists", exists);
    }

    @Test
    public void existsByUsername_UserExists_ReturnsTrue() {
        boolean exists = userRepository.existsByUsername(user.getUsername());
        assertTrue(exists);
    }

    @Test
    public void existsByUsername_UserDoesNotExist_ReturnsFalse() {
        boolean exists = userRepository.existsByUsername("InvalidUsername");
        assertFalse("User exists", exists);
    }

    @Test
    public void existsByUserId_UserDoesNotExist_ReturnsFalse() {
        boolean exists = userRepository.existsByUserID(UUID.randomUUID());
        assertFalse("User Exists", exists);
    }

    @Test
    public void existsByUserId_UserExists_ReturnsTrue() {
        boolean exists = userRepository.existsByUserID(user.getUserID());
        assertTrue(exists, "User does not exist");
    }
    // Deletion Tests
    @Test
    public void deleteByUserId_UserIdDoesNotExist_ReturnsTrue() {
        userRepository.deleteByUserID(UUID.randomUUID());
        boolean exists = userRepository.existsByUserID(user.getUserID());
        assertTrue(exists);
    }

    @Test
    public void deleteByUserId_UserExists_ReturnsFalse() {
        userRepository.deleteByUserID(user.getUserID());
        boolean exists = userRepository.existsByUserID(user.getUserID());
        assertFalse("User not deleted", exists);
    }
    //????

    @Test
    public void findAllByUserType() {
        List<User> users = userRepository.findAllByUserType(UserType.ROLE_ACADEMIC);
        assertNotNull("Null Users", users);
    }
}
