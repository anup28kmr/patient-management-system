package com.ak.patientservice.mapper;

import com.ak.patientservice.dto.PatientRequestDto;
import com.ak.patientservice.dto.PatientResponseDto;
import com.ak.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
  public static PatientResponseDto patientToPatientResponseDto(Patient patient) {
    PatientResponseDto patientResponseDto = new PatientResponseDto();
    patientResponseDto.setId(patient.getId().toString());
    patientResponseDto.setEmail(patient.getEmail());
    patientResponseDto.setName(patient.getName());
    patientResponseDto.setAddress(patient.getAddress());
    patientResponseDto.setDateOfBirth(patient.getDateOfBirth().toString());
    return patientResponseDto;
  }

  public static Patient toPatientModel(PatientRequestDto patientRequestDto) {
    Patient patient = new Patient();
    patient.setName(patientRequestDto.getName());
    patient.setEmail(patientRequestDto.getEmail());
    patient.setAddress(patientRequestDto.getAddress());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));
    patient.setRegisteredDate(LocalDate.parse(patientRequestDto.getRegisteredDate()));
    return patient;
  }
}
