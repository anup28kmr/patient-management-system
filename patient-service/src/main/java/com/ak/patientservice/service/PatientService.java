package com.ak.patientservice.service;

import com.ak.patientservice.dto.PatientRequestDto;
import com.ak.patientservice.dto.PatientResponseDto;
import com.ak.patientservice.exceptions.EmailAlreadyExistsException;
import com.ak.patientservice.mapper.PatientMapper;
import com.ak.patientservice.model.Patient;
import com.ak.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

  private final PatientRepository patientRepository;

  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public List<PatientResponseDto> findAll() {
    return patientRepository.findAll().stream().map(PatientMapper::patientToPatientResponseDto).collect(Collectors.toList());
  }

  public PatientResponseDto createPatient(PatientRequestDto dto) {
    if(patientRepository.existsByEmail(dto.getEmail())) {
      throw new EmailAlreadyExistsException("Patient already exists with this email :: " + dto.getEmail());
    }
    Patient patient = patientRepository.save(PatientMapper.toPatientModel(dto));
    return PatientMapper.patientToPatientResponseDto(patient);
  }
}
