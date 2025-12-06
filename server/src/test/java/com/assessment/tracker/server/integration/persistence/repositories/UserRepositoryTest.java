package com.assessment.tracker.server.integration.persistence.repositories;

import com.assessment.tracker.server.persistence.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {

    }
    @Test
    public void testFindByEmail() {

    }
    @Test
    public void testFindByUserId() {
    }
    @Test
    public void testExistsByUserId() {

    }
    @Test
    public void testExistsByEmail() {

    }
    @Test
    public void testDeleteByUserId() {

    }
    @Test
    public void testFindAllByUserType() {

    }
}
