package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.dto.ModuleRoleDTO;
import com.assessment.tracker.server.persistence.entities.ModuleRole;
import com.assessment.tracker.server.persistence.repos.ModuleRolesRepo;
import com.assessment.tracker.server.app.mappers.ModuleRoleMapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleRolesService {

    private final ModuleRolesRepo moduleRolesRepo;
    private final ModuleRoleMapper moduleRoleMapper;

    @Autowired
    public ModuleRolesService(ModuleRolesRepo moduleRolesRepo, ModuleRoleMapper moduleRoleMapper) {
        this.moduleRolesRepo = moduleRolesRepo;
        this.moduleRoleMapper = moduleRoleMapper;
    }

    public void update(UUID id, ModuleRole newModuleRole) {
        Optional<ModuleRole> existing = Optional.ofNullable((ModuleRole) moduleRolesRepo.findById(id).orElse(null));

        if (existing.isPresent()) {
            newModuleRole.setID(existing.get().getID());
            moduleRolesRepo.save(newModuleRole);
        } else {
            throw new EntityNotFoundException("ModuleRole not found with id: " + id);
        }
    }

    public void save(ModuleRole moduleRole) {
        moduleRolesRepo.save(moduleRole);
    }

    public ModuleRoleDTO getDTOById(UUID id) {
        ModuleRole role = (ModuleRole) moduleRolesRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ModuleRole not found with id: " + id));

        return moduleRoleMapper.entityToApi(role);
    }

    public List<ModuleRoleDTO> retrieveAll() {
        List<ModuleRole> list = moduleRolesRepo.findAll();
        List<ModuleRoleDTO> dtos = new ArrayList<>();

        for (ModuleRole role : list) {
            dtos.add(moduleRoleMapper.entityToApi(role));
        }

        return dtos;
    }

    //Retrieve all specific module roles

    public void delete(UUID id) {
        moduleRolesRepo.deleteById(id);
    }
}
