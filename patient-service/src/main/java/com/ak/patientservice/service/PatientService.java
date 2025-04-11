package com.ak.patientservice.service;

import com.ak.patientservice.dto.PatientRequestDto;
import com.ak.patientservice.dto.PatientResponseDto;
import com.ak.patientservice.exceptions.EmailAlreadyExistsException;
import com.ak.patientservice.grpc.BillingServiceGrpcClient;
import com.ak.patientservice.mapper.PatientMapper;
import com.ak.patientservice.model.Patient;
import com.ak.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;

  public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
    this.patientRepository = patientRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
  }

  public List<PatientResponseDto> findAll() {
    return patientRepository.findAll().stream().map(PatientMapper::patientToPatientResponseDto).collect(Collectors.toList());
  }

  public PatientResponseDto createPatient(PatientRequestDto dto) {
    if(patientRepository.existsByEmail(dto.getEmail())) {
      throw new EmailAlreadyExistsException("Patient already exists with this email :: " + dto.getEmail());
    }
    Patient patient = patientRepository.save(PatientMapper.toPatientModel(dto));

    billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),patient.getName(),patient.getEmail());

    return PatientMapper.patientToPatientResponseDto(patient);
  }
}
