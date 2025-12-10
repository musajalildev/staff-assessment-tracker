package com.assessment.tracker.server.unit.services;
import com.assessment.tracker.server.api.dto.ModuleDTO;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.app.mappers.ModuleMapper;
import com.assessment.tracker.server.persistence.repos.ModuleRepo;
import com.assessment.tracker.server.persistence.services.ModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private ModuleRepo moduleRepository;

    @Mock
    private ModuleMapper moduleMapper;

    @InjectMocks
    private ModuleService moduleService;

    private Module module;
    private ModuleDTO moduleDTO;
    private UUID moduleId;

    @BeforeEach
    void setUp() {
        moduleId = UUID.randomUUID();

        module = new Module();
        module.setID(moduleId);
        module.setCode("CS101");

        moduleDTO = new ModuleDTO();
        moduleDTO.setCode("CS101");
    }

    @Test
    void update_whenModuleExists_shouldUpdateSuccessfully() {
        Module existingModule = new Module();
        existingModule.setID(moduleId);

        Module newModule = new Module();
        newModule.setCode("CS102");

        when(moduleRepository.findById(moduleId)).thenReturn(existingModule);

        moduleService.update(moduleId, newModule);

        assertEquals(moduleId, newModule.getID());
        verify(moduleRepository).findById(moduleId);
        verify(moduleRepository).save(newModule);
    }

    @Test
    void save_shouldSaveModuleSuccessfully() {
        moduleService.save(module);

        verify(moduleRepository, times(1)).save(module);
    }

    @Test
    void getModuleDTOListByCode_shouldReturnModuleDTO() {
        String moduleCode = "CS101";

        when(moduleRepository.findByCode(moduleCode)).thenReturn(module);
        when(moduleMapper.entityToApi(module)).thenReturn(moduleDTO);

        ModuleDTO result = moduleService.getModuleDTOListByCode(moduleCode);

        assertNotNull(result);
        assertEquals(moduleDTO.getCode(), result.getCode());
        verify(moduleRepository).findByCode(moduleCode);
        verify(moduleMapper).entityToApi(module);
    }

    @Test
    void retrieveModules_shouldReturnListOfModuleDTOs() {
        Module module2 = new Module();
        module2.setID(UUID.randomUUID());
        module2.setCode("CS102");

        ModuleDTO moduleDTO2 = new ModuleDTO();
        moduleDTO2.setCode("CS102");

        List<Module> moduleList = Arrays.asList(module, module2);

        when(moduleRepository.findAll()).thenReturn(moduleList);
        when(moduleMapper.entityToApi(module)).thenReturn(moduleDTO);
        when(moduleMapper.entityToApi(module2)).thenReturn(moduleDTO2);

        List<ModuleDTO> result = moduleService.retrieveModules();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CS101", result.get(0).getCode());
        assertEquals("CS102", result.get(1).getCode());
        verify(moduleRepository).findAll();
        verify(moduleMapper, times(2)).entityToApi(any(Module.class));
    }

    @Test
    void retrieveModules_whenNoModulesExist_shouldReturnEmptyList() {
        when(moduleRepository.findAll()).thenReturn(Arrays.asList());

        List<ModuleDTO> result = moduleService.retrieveModules();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(moduleRepository).findAll();
        verify(moduleMapper, never()).entityToApi(any(Module.class));
    }
}
