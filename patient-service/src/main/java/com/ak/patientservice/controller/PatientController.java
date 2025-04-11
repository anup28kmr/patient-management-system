package com.ak.patientservice.controller;

import com.ak.patientservice.dto.PatientRequestDto;
import com.ak.patientservice.dto.PatientResponseDto;
import com.ak.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/patients")
@Tag(name = "Patient", description = "API for managing Patients")
public class PatientController {

  private static final Logger log = LoggerFactory.getLogger(PatientController.class);
  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  @Operation(summary = "Get Patients")
  public List<PatientResponseDto> getPatients() {
    return patientService.findAll();
  }

  @PostMapping
  @Operation(summary = "Create a new Patient")
  public PatientResponseDto createPatient(@Valid @RequestBody PatientRequestDto dto) {
    log.info("Request received to create a new patient with email :: {}", dto.getEmail());
    return patientService.createPatient(dto);
  }
}
