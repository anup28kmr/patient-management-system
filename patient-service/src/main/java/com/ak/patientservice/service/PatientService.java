package com.ak.patientservice.service;

import com.ak.patientservice.dto.PatientRequestDto;
import com.ak.patientservice.dto.PatientResponseDto;
import com.ak.patientservice.exceptions.EmailAlreadyExistsException;
import com.ak.patientservice.grpc.BillingServiceGrpcClient;
import com.ak.patientservice.kafka.KafkaProducer;
import com.ak.patientservice.mapper.PatientMapper;
import com.ak.patientservice.model.Patient;
import com.ak.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

  private static final Logger log = LoggerFactory.getLogger(PatientService.class);
  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;

  public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
    this.patientRepository = patientRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
    this.kafkaProducer = kafkaProducer;
  }

  public List<PatientResponseDto> findAll() {
    return patientRepository.findAll().stream().map(PatientMapper::patientToPatientResponseDto).collect(Collectors.toList());
  }

  public PatientResponseDto createPatient(PatientRequestDto dto) {
    if(patientRepository.existsByEmail(dto.getEmail())) {
      log.error("Patient with email : {} already exists", dto.getEmail());
      throw new EmailAlreadyExistsException("Patient already exists with this email :: " + dto.getEmail());
    }
    Patient newPatient = patientRepository.save(PatientMapper.toPatientModel(dto));

    billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),newPatient.getName(),newPatient.getEmail());

    kafkaProducer.sendEvent(newPatient);
    return PatientMapper.patientToPatientResponseDto(newPatient);
  }
}
