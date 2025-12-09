package com.assessment.tracker.server;

import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.UserRepository;
import com.assessment.tracker.server.utils.enums.UserType;
import com.assessment.tracker.server.app.mappers.CsvMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ServerApplicationTests {

    @Autowired
    private CsvMapper csvMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void csvMapper_correctlyMapsModulesUsersAndAssessments() {

        // Ensure test isolation
        userRepository.deleteAll();

        // Create unique test users
        User leader = new User("leaderUser", "password", "leader@test.com", UserType.ACADEMIC);
        userRepository.save(leader);

        User staff1 = new User("staffA", "password", "staffA@test.com", UserType.ACADEMIC);
        User staff2 = new User("staffB", "password", "staffB@test.com", UserType.ACADEMIC);
        userRepository.save(staff1);
        userRepository.save(staff2);

        String csvContent =
                "12345,Intro to Computing,leaderUser,staffA|staffB,EXAM,Final Exam,COURSEWORK,Essay 1";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "modules.csv",
                "text/csv",
                csvContent.getBytes()
        );

        List<Module> modules = csvMapper.apiToEntity(file);

        assertEquals(1, modules.size());
        Module m = modules.get(0);

        assertEquals(12345, m.getCode());
        assertEquals("Intro to Computing", m.getTitle());

        assertEquals(leader.getUserID(), m.getLeaderID());
        assertTrue(m.getOtherStaffIDs().contains(staff1.getUserID()));
        assertTrue(m.getOtherStaffIDs().contains(staff2.getUserID()));

        assertEquals(2, m.getAssessments().size());
        assertEquals("Final Exam", m.getAssessments().get(0).getTitle());
        assertEquals("Essay 1", m.getAssessments().get(1).getTitle());
    }
}

