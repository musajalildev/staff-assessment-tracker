package com.assessment.tracker.server.integration.services;

import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.UserRepository;
import com.assessment.tracker.server.persistence.services.UserService;
import com.assessment.tracker.server.utils.enums.UserType;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
// Remove this if unused:
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("testuser", "test@example.com", "testpass", UserType.ROLE_ACADEMIC);
        userRepo.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        if (testUser.getUserID() != null && userRepo.existsByUserID(testUser.getUserID())) {
            userRepo.deleteByUserID(testUser.getUserID());
        }
    }

    @Test
    @Order(1)
    public void testGetUserById() {
        User result = userService.getUser(testUser.getUserID());
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    @Order(2)
    public void testUpdateUsername() {
        userService.updateUsername("updatedUser", testUser.getUserID());
        User updated = userRepo.findByUserID(testUser.getUserID());
        assertEquals("updatedUser", updated.getUsername());
    }

    @Test
    @Order(3)
    public void testUpdateUserEmail() {
        userService.updateUserEmail("updated@example.com", testUser.getUserID());
        User updated = userRepo.findByUserID(testUser.getUserID());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    @Order(4)
    public void testDeleteUser() {
        boolean deleted = userService.deleteUser(testUser.getUserID());
        assertTrue(deleted);
        assertFalse(userRepo.existsByUserID(testUser.getUserID()));
    }

    @Test
    @Order(5)
    public void testGetAllUsersByPermission() {
        List<User> academics = userService.getAllUsersByPermission(UserType.ROLE_ACADEMIC);
        assertTrue(academics.stream().anyMatch(u -> u.getUsername().equals("testuser")));
    }

    @Test
    @Order(6)
    public void testGetUserByUsername() {
        User found = userService.getUserByUsername("testuser");
        assertNotNull(found);
        assertEquals(testUser.getEmail(), found.getEmail());
    }

    @Test
    @Order(7)
    public void testGetUserByEmail() {
        User found = userService.getUserByEmail("test@example.com");
        assertNotNull(found);
        assertEquals(testUser.getUsername(), found.getUsername());
    }
}
