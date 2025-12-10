package com.assessment.tracker.server.unit.repositories;

import com.assessment.tracker.server.persistence.entities.AssignedUser;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.AssignedUserRepository;
import com.assessment.tracker.server.utils.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
public class AssignedUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AssignedUserRepository assignedUserRepository;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    void testFindByUser() {
    }

    @Test
    void testFindByRole() {
    }

    @Test
    void testExistsByUserAndRole() {
    }

    @Test
    void testFindAllByRole() {

    }

    @Test
    void testFindAllByUser(){
    }
}
