package com.assessment.tracker.server.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;

public interface AssessmentController {
        @GetMapping(produces = "application/json", path = "/api/assessment/all")
        ResponseEntity<List<AssessmentDTO>> getAllAssessments();

        @GetMapping(produces = "application/json", path = "/api/assessment/{id}")
        ResponseEntity<AssessmentDTO> getAssessment(@PathVariable int id);

        @PostMapping(consumes = "application/json", produces = "application/json", path = "/api/v1/assessment")
        ResponseEntity<AssessmentDTO> createAssessment(
                        @RequestBody AssessmentDTO assessmentDTO,
                        @AuthenticationPrincipal AuthorisedUser user);

        @PutMapping(consumes = "application/json", produces = "application/json", path = "/api/assessment/{id}")
        ResponseEntity<AssessmentDTO> updateAssessment(
                        @PathVariable int id,
                        @RequestBody AssessmentDTO assessmentDTO,
                        Authentication auth);
}
