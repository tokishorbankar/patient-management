package com.pm.patientservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.model.entities.Patient;
import com.pm.patientservice.model.mapper.PatientMapper;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.utility.PatientUtil;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTests {

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private PatientMapper patientMapper;

  @InjectMocks
  private PatientService patientService;


  @Test
  @DisplayName("should retrieve all patients when repository returns empty list")
  void shouldRetrieveAllPatientsWhenRepositoryReturnsEmptyList() {
    when(patientRepository.findAll()).thenReturn(List.of());

    List<PatientDTO> patients = patientService.getAllPatients();

    assertEquals(0, patients.size());
    verify(patientMapper, times(0)).toDto(any(Patient.class));
    verify(patientRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("should retrieve all patients when repository returns non-empty list")
  void shouldRetrieveAllPatientsWhenRepositoryReturnsNonEmptyList() {
    List<Patient> existingEntities = Stream.generate(PatientUtil::buildRandomPatient)
        .limit(5).toList();

    when(patientRepository.findAll()).thenReturn(existingEntities);

    List<PatientDTO> patients = patientService.getAllPatients();

    assertEquals(existingEntities.size(), patients.size());
    verify(patientMapper,
        times(existingEntities.size())).toDto(any(Patient.class));
    verify(patientRepository, times(1)).findAll();

  }

  @Test
  @DisplayName("should retrieve patient by ID when exists")
  void shouldRetrievePatientByIdWhenExists() {
    UUID patientId = UUID.randomUUID();
    Patient existingPatient = PatientUtil.buildRandomPatient();
    existingPatient.setId(patientId);
    PatientDTO expectedPatientDTO = PatientUtil.buildRandomPatientDTO();

    when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
    when(patientMapper.toDto(existingPatient)).thenReturn(expectedPatientDTO);

    PatientDTO actualPatientDTO = patientService.getPatientById(patientId);

    assertEquals(expectedPatientDTO, actualPatientDTO);
    assertNotNull(actualPatientDTO);
    verify(patientRepository, times(1)).findById(patientId);
    verify(patientMapper, times(1)).toDto(existingPatient);
  }

  @Test
  @DisplayName("should throw exception when retrieving patient by non-existent ID")
  void shouldThrowExceptionWhenRetrievingPatientByNonExistentId() {
    UUID nonExistentId = UUID.randomUUID();
    when(patientRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class,
        () -> patientService.getPatientById(nonExistentId));
    verify(patientRepository, times(1)).findById(nonExistentId);

  }

  @Test
  @DisplayName("should throw exception when retrieving patient by non-existent email")
  void shouldThrowExceptionWhenRetrievingPatientByNonExistentEmail() {
    String nonExistentEmail = "nonexistent@example.com";
    when(patientRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class,
        () -> patientService.getPatientByEmail(nonExistentEmail));

    verify(patientRepository, times(1)).findByEmail(nonExistentEmail);
  }

  @Test
  @DisplayName("should throw exception when creating patient with existing email")
  void shouldThrowExceptionWhenCreatingPatientWithExistingEmail() {
    PatientDTO patientDTO = new PatientDTO();
    patientDTO.setEmail("existing@example.com");
    when(patientRepository.existsByEmail(patientDTO.getEmail())).thenReturn(true);

    assertThrows(EmailAlreadyExistsException.class,
        () -> patientService.createPatient(patientDTO));
    verify(patientRepository, times(1)).existsByEmail(patientDTO.getEmail());

  }

  @Test
  @DisplayName("should throw exception when updating patient with non-existent ID")
  void shouldThrowExceptionWhenUpdatingPatientWithNonExistentId() {
    UUID nonExistentId = UUID.randomUUID();
    PatientDTO patientDTO = new PatientDTO();
    patientDTO.setEmail("update@example.com");
    when(patientRepository.existsById(nonExistentId)).thenReturn(false);

    assertThrows(PatientNotFoundException.class,
        () -> patientService.updatePatient(nonExistentId, patientDTO));
    verify(patientRepository, times(1)).existsById(nonExistentId);

  }

  @Test
  @DisplayName("should throw exception when updating patient with existing email")
  void shouldThrowExceptionWhenUpdatingPatientWithExistingEmail() {
    UUID patientId = UUID.randomUUID();
    PatientDTO patientDTO = new PatientDTO();
    patientDTO.setEmail("existing@example.com");
    when(patientRepository.existsById(patientId)).thenReturn(true);
    when(patientRepository.existsByEmailAndIdNot(patientDTO.getEmail(), patientId)).thenReturn(
        true);

    assertThrows(EmailAlreadyExistsException.class,
        () -> patientService.updatePatient(patientId, patientDTO));
    verify(patientRepository, times(1)).existsById(patientId);
    verify(patientRepository, times(1)).existsByEmailAndIdNot(patientDTO.getEmail(), patientId);
    verify(patientMapper, times(0)).toEntity(any(PatientDTO.class));
  }

  @Test
  @DisplayName("should throw exception when deleting patient by non-existent ID")
  void shouldThrowExceptionWhenDeletingPatientByNonExistentId() {
    UUID nonExistentId = UUID.randomUUID();
    when(patientRepository.existsById(nonExistentId)).thenReturn(false);

    assertThrows(PatientNotFoundException.class,
        () -> patientService.deletePatient(nonExistentId));
    verify(patientRepository, times(1)).existsById(nonExistentId);
  }

  @Test
  @DisplayName("should throw exception when deleting patient by non-existent email")
  void shouldThrowExceptionWhenDeletingPatientByNonExistentEmail() {
    String nonExistentEmail = "nonexistent@example.com";
    when(patientRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class,
        () -> patientService.deletePatientByEmail(nonExistentEmail));
    verify(patientRepository, times(1)).findByEmail(nonExistentEmail);
  }

}
