package com.assessment.tracker.server.integration.services;

import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.ModuleRepo;
import com.assessment.tracker.server.persistence.services.ModuleService;
import com.assessment.tracker.server.api.dto.ModuleDTO;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModuleServiceIntegrationTest {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleRepo moduleRepo;

    private Module testModule;

    @BeforeEach
    public void setUp() {
        testModule = new Module();
        testModule.setCode("CS101");
        testModule.setTitle("Intro to Computer Science");
        testModule.setArchived(false);

        moduleService.save(testModule);
    }

    @AfterEach
    public void tearDown() {
        moduleRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testSaveModule() {
        Module found = moduleRepo.findByCode("CS101");
        assertNotNull(found);
        assertEquals("CS101", found.getCode());
    }

    @Test
    @Order(2)
    public void testGetModuleByCode() {
        ModuleDTO dto = moduleService.getModuleDTOListByCode("CS101");
        assertNotNull(dto);
        assertEquals("CS101", dto.getCode());
    }

    @Test
    @Order(3)
    public void testRetrieveModules() {
        List<ModuleDTO> list = moduleService.retrieveModules();
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(4)
    public void testUpdateModule() {
        // Create new data for update
        Module updated = new Module();
        updated.setCode("CS102");
        updated.setTitle("Updated Module Title");
        updated.setArchived(true);

        UUID id = testModule.getID();
        moduleService.update(id, updated);

        Module result = moduleRepo.findByID(id);
        assertNotNull(result);
        assertEquals("CS102", result.getCode());
        assertEquals("Updated Module Title", result.getTitle());
        assertTrue(result.getArchiveStatus());
    }
}
