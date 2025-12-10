package com.assessment.tracker.server.unit.services;

import com.assessment.tracker.server.api.dto.ModuleRoleDTO;
import com.assessment.tracker.server.app.mappers.ModuleRoleMapper;
import com.assessment.tracker.server.persistence.entities.ModuleRole;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.repos.ModuleRolesRepo;
import com.assessment.tracker.server.persistence.services.ModuleRolesService;
import com.assessment.tracker.server.utils.enums.ModuleRoles;
import com.assessment.tracker.server.utils.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleRolesServiceTest {

    @Mock
    private ModuleRolesRepo moduleRolesRepo;

    @Mock
    private ModuleRoleMapper moduleRoleMapper;

    @InjectMocks
    private ModuleRolesService moduleRolesService;

    private ModuleRole moduleRole;
    private ModuleRoleDTO moduleRoleDTO;
    private UUID roleId;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();

        moduleRole = new ModuleRole();
        moduleRole.setID(roleId);
        moduleRole.setRole(ModuleRoles.ROLE_MODULE_LEAD);
        moduleRole.setUser(
                new User(
                        "username",
                        "email@email.com",
                        "pwd",
                        UserType.ROLE_ACADEMIC));
    }

    @Test
    void update_whenModuleRoleExists_shouldUpdateSuccessfully() {
        ModuleRole existingRole = new ModuleRole();
        existingRole.setID(roleId);

        ModuleRole newRole = new ModuleRole();
        newRole.setRole(ModuleRoles.ROLE_MODULE_STAFF);

        when(moduleRolesRepo.findById(roleId)).thenReturn(Optional.of(existingRole));

        moduleRolesService.update(roleId, newRole);

        assertEquals(roleId, newRole.getID());
        verify(moduleRolesRepo).findById(roleId);
        verify(moduleRolesRepo).save(newRole);
    }

    @Test
    void save_shouldSaveModuleRoleSuccessfully() {
        moduleRolesService.save(moduleRole);

        verify(moduleRolesRepo, times(1)).save(moduleRole);
    }

    @Test
    void getDTOById_whenModuleRoleExists_shouldReturnDTO() {

        ModuleRoleDTO result = moduleRolesService.getDTOById(roleId);

        assertNotNull(result);
        assertEquals(ModuleRoles.ROLE_MODULE_LEAD, result.getRole());
        verify(moduleRolesRepo).findById(roleId);
        verify(moduleRoleMapper).entityToApi(moduleRole);
    }

    @Test
    void retrieveAll_shouldReturnListOfModuleRoleDTOs() {
        List<ModuleRole> roleList = Arrays.asList(moduleRole);

        when(moduleRolesRepo.findAll()).thenReturn(roleList);
        when(moduleRoleMapper.entityToApi(moduleRole)).thenReturn(moduleRoleDTO);

        List<ModuleRoleDTO> result = moduleRolesService.retrieveAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moduleRolesRepo).findAll();
        verify(moduleRoleMapper, times(1)).entityToApi(any(ModuleRole.class));
    }

    @Test
    void retrieveAll_whenNoRolesExist_shouldReturnEmptyList() {
        when(moduleRolesRepo.findAll()).thenReturn(Arrays.asList());

        List<ModuleRoleDTO> result = moduleRolesService.retrieveAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(moduleRolesRepo).findAll();
        verify(moduleRoleMapper, never()).entityToApi(any(ModuleRole.class));
    }

    @Test
    void delete_shouldDeleteModuleRoleSuccessfully() {
        moduleRolesService.delete(roleId);

        verify(moduleRolesRepo, times(1)).deleteById(roleId);
    }
}
