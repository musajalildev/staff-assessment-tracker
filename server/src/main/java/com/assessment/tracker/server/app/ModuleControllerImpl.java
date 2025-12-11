package com.assessment.tracker.server.app;

import com.assessment.tracker.server.api.controller.ModuleController;
import com.assessment.tracker.server.api.dto.ModuleDTO;
import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.entities.ModuleRole;
import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.logging.ModuleLogRepository;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.ModuleService;
import com.assessment.tracker.server.persistence.services.logging.ModuleLogService;
import com.assessment.tracker.server.app.mappers.ModuleMapper;
import com.assessment.tracker.server.app.mappers.CsvMapper;
import com.assessment.tracker.server.utils.enums.AssessmentActions;
import com.assessment.tracker.server.utils.enums.ModuleRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.oauth2.jwt.*;
import com.assessment.tracker.server.utils.enums.*;
import java.util.List;
import java.util.UUID;

@RestController
public class ModuleControllerImpl implements ModuleController {

    private final ModuleMapper moduleMapper;
    private final ModuleService moduleService;
    private final ModuleLogService moduleLogService;
    private final UserRepository userRepository;
    private final CsvMapper csvMapper;

    @Autowired
    public ModuleControllerImpl(ModuleMapper moduleMapper, ModuleService moduleService,
            ModuleLogService moduleLogService, CsvMapper csvMapper, UserRepository userRepository) {
        this.moduleMapper = moduleMapper;
        this.moduleService = moduleService;
        this.moduleLogService = moduleLogService;
        this.csvMapper = csvMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<ModuleDTO>> getAllModules(Authentication auth) {
        List<ModuleDTO> dtos = moduleService.retrieveModules();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<ModuleDTO> getFirstModuleByCode(String moduleCode, Authentication auth) {
        ModuleDTO dtos = moduleService.getModuleDTOListByCode(moduleCode);
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<ModuleDTO> updateModule(UUID id, ModuleDTO dto, Authentication auth) {
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!(user.getUserType() == UserType.ROLE_EXAMS_OFFICER
                || user.getUserType() == UserType.ROLE_TEACHING_SUPPORT)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Module entity = moduleMapper.apiToEntity(dto);
        ModuleLog log = new ModuleLog();
        log.setActionType(ModuleActions.UPDATE);
        log.setTargetModule(entity);
        log.setUser(user);
        moduleLogService.save(log);
        moduleService.update(id, entity);

        return ResponseEntity.ok(moduleMapper.entityToApi(entity));
    }

    @Override
    public ResponseEntity<ModuleDTO> createModule(ModuleDTO module, Authentication auth) {
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!(user.getUserType() == UserType.ROLE_EXAMS_OFFICER
                || user.getUserType() == UserType.ROLE_TEACHING_SUPPORT)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Module entity = moduleMapper.apiToEntity(module);
        ModuleLog log = new ModuleLog();
        log.setActionType(ModuleActions.CREATE);
        log.setTargetModule(entity);
        log.setUser(user);
        moduleLogService.save(log);
        moduleService.save(moduleMapper.apiToEntity(module));
        return ResponseEntity.ok(module);
    }

    @Override
    public ResponseEntity<Void> createModulesFromCSV(MultipartFile file, String uploader, Authentication auth) {
        User user = new User();
        try {
            user = userRepository.findByUsername(((Jwt) auth.getPrincipal()).getClaimAsString("sub"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!(user.getUserType() == UserType.ROLE_EXAMS_OFFICER
                || user.getUserType() == UserType.ROLE_TEACHING_SUPPORT)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        if (!originalName.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().build();
        }

        List<Module> newModuleList = csvMapper.apiToEntity(file);
        ModuleLog log = new ModuleLog();
        for (Module entity : newModuleList) {
            log = new ModuleLog();
            log.setActionType(ModuleActions.CREATE);
            log.setTargetModule(entity);
            log.setUser(user);
            moduleLogService.save(log);
        }
        return (ResponseEntity<Void>) ResponseEntity.ok();
    }
}
