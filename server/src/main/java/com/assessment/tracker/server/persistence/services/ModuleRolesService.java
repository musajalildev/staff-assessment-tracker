package com.assessment.tracker.server.persistence.services;

import com.assessment.tracker.server.api.DTO.ModuleRoleDTO;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.entities.ModuleRole;
import com.assessment.tracker.server.persistence.repos.ModuleRolesRepo;
import com.assessment.tracker.server.utils.mappers.ModuleRoleMapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleRolesService {

    @Autowired
    private ModuleRolesRepo ModuleRolesRepo;
    private ModuleRoleMapper moduleRoleMapper;

    public void update(UUID id, ModuleRole newModuleRole) {
        Optional<ModuleRole> existing = Optional.ofNullable((ModuleRole) ModuleRolesRepo.findById(id).orElse(null));

        if (existing.isPresent()) {
            newModuleRole.setID(existing.get().getID());
            ModuleRolesRepo.save(newModuleRole);
        } else {
            throw new EntityNotFoundException("ModuleRole not found with id: " + id);
        }
    }

    public void save(ModuleRole moduleRole) {
        ModuleRolesRepo.save(moduleRole);
    }

    public ModuleRoleDTO getDTOById(UUID id) {
        ModuleRole role = (ModuleRole) ModuleRolesRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ModuleRole not found with id: " + id));

        return moduleRoleMapper.entityToApi(role);
    }

    public List<ModuleRoleDTO> retrieveAll() {
        List<ModuleRole> list = ModuleRolesRepo.findAll();
        List<ModuleRoleDTO> dtos = new ArrayList<>();

        for (ModuleRole role : list) {
            dtos.add(moduleRoleMapper.entityToApi(role));
        }

        return dtos;
    }

    public void delete(UUID id) {
        ModuleRolesRepo.deleteById(id);
    }
}
