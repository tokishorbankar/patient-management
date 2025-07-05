package com.pm.patientservice.service;

import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.model.entities.Patient;
import com.pm.patientservice.model.mapper.PatientMapper;
import com.pm.patientservice.repository.PatientRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientService {

  private static final Logger log = LoggerFactory.getLogger(PatientService.class);
  /**
   * Error messages for various exceptions.
   */
  public static final String ERROR_MESSAGE_NOT_FOUND_BY_ID = "Patient not found with ID: %s";
  public static final String ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID = "Patient not found with email: "
      + "%s";
  public static final String ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID = "Patient already exists "
      + "with "
      + "the provided email address %s";

  private final PatientMapper patientMapper;

  private final PatientRepository patientRepository;

  /**
   * Constructor for PatientService.
   *
   * @param patientMapper     the mapper to convert between Patient and PatientDTO
   * @param patientRepository the repository for accessing patient data
   */
  public PatientService(final PatientMapper patientMapper,
      final PatientRepository patientRepository) {
    this.patientMapper = patientMapper;
    this.patientRepository = patientRepository;
  }

  /**
   * Retrieves all patients.
   *
   * @return a list of PatientDTOs representing all patients
   */
  public List<PatientDTO> getAllPatients() {
    log.info("Retrieving all patients");
    return patientRepository.findAll().stream()
        .map(patientMapper::toDto)
        .toList();
  }

  /**
   * Retrieves a patient by their ID.
   *
   * @param id the ID of the patient to retrieve
   * @return the PatientDTO representing the patient, or null if not found
   */
  public PatientDTO getPatientById(final UUID id) {
    log.info("Retrieving patient with ID: {}", id);
    return patientRepository.findById(id)
        .map(patientMapper::toDto)
        .orElseThrow(
            () -> new PatientNotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, id)));
  }

  /**
   * Retrieves a patient by their email.
   *
   * @param email the email of the patient to retrieve
   * @return the PatientDTO representing the patient, or null if not found
   */
  public PatientDTO getPatientByEmail(final String email) {
    log.info("Retrieving patient with email: {}", email);
    return patientRepository.findByEmail(email)
        .map(patientMapper::toDto)
        .orElseThrow(() -> new PatientNotFoundException(
            String.format(ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID, email)));
  }

  /**
   * Creates a new patient.
   *
   * @param patientDTO the patient data transfer object containing patient details
   * @return the created patient as a data transfer object
   */
  public PatientDTO createPatient(final PatientDTO patientDTO) {
    log.info("Creating patient with email: {}", patientDTO.getEmail());

    if (patientRepository.existsByEmail(patientDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          String.format(ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID, patientDTO.getEmail()));
    }

    Patient saved = patientRepository.save(patientMapper.toEntity(patientDTO));
    return patientMapper.toDto(saved);

  }

  /**
   * Updates an existing patient.
   *
   * @param id         the ID of the patient to update
   * @param patientDTO the updated patient data transfer object
   * @return the updated patient as a data transfer object
   */
  public PatientDTO updatePatient(final UUID id, final PatientDTO patientDTO) {
    log.info("Updating patient with ID: {}", id);

    if (!patientRepository.existsById(id)) {
      throw new PatientNotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, id));
    }

    if (patientRepository.existsByEmailAndIdNot(patientDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException(
          String.format(ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID,
              patientDTO.getEmail()));
    }

    patientDTO.setId(id);
    Patient updated = patientRepository.save(patientMapper.toEntity(patientDTO));
    return patientMapper.toDto(updated);
  }

  /**
   * Deletes a patient by their ID.
   *
   * @param id the ID of the patient to delete
   */
  public void deletePatient(final UUID id) {
    log.info("Deleting patient with ID: {}", id);
    if (!patientRepository.existsById(id)) {
      throw new PatientNotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, id));
    }
    patientRepository.deleteById(id);
  }

  /**
   * Deletes a patient by their email.
   *
   * @param email the email of the patient to delete
   */
  public void deletePatientByEmail(final String email) {
    log.info("Deleting patient with email: {}", email);
    Patient patient = patientRepository.findByEmail(email)
        .orElseThrow(() -> new PatientNotFoundException(
            String.format(ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID, email)));
    patientRepository.delete(patient);
  }


}
