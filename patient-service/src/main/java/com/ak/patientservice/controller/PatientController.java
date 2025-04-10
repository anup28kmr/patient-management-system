package com.ak.patientservice.controller;

import com.ak.patientservice.dto.PatientRequestDto;
import com.ak.patientservice.dto.PatientResponseDto;
import com.ak.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  public List<PatientResponseDto> getPatients() {
    return patientService.findAll();
  }

  @PostMapping
  public PatientResponseDto createPatient(@Valid @RequestBody PatientRequestDto dto) {
    return patientService.createPatient(dto);
  }
}
