package com.pm.patientservice.controller;

import com.pm.patientservice.model.dto.APIResponse;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.model.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.constraints.Email;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {

  private final PatientService patientService;

  /**
   * Constructor for PatientController.
   *
   * @param patientService the service to handle patient-related operations
   */

  public PatientController(final PatientService patientService) {
    this.patientService = patientService;
  }

  /**
   * Retrieves all patients.
   *
   * @return ResponseEntity with APIResponse containing a list of PatientDTOs
   */
  @GetMapping
  public ResponseEntity<APIResponse<List<PatientDTO>>> getAllPatients() {
    List<PatientDTO> patients = patientService.getAllPatients();
    return ResponseEntity.ok().body(new APIResponse<>(patients));
  }

  /**
   * Retrieves a patient by ID.
   *
   * @param id the ID of the patient to retrieve
   * @return ResponseEntity with APIResponse containing the PatientDTO
   */
  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<PatientDTO>> getPatientById(@PathVariable("id") final UUID id) {
    PatientDTO patient = patientService.getPatientById(id);
    return ResponseEntity.ok().body(new APIResponse<>(patient));
  }

  /**
   * Retrieves a patient by email.
   *
   * @param email the email of the patient to retrieve
   * @return ResponseEntity with APIResponse containing the PatientDTO
   */
  @GetMapping("/email/{email}")
  public ResponseEntity<APIResponse<PatientDTO>> getPatientByEmail(
      @PathVariable("email") final String email) {
    PatientDTO patient = patientService.getPatientByEmail(email);
    return ResponseEntity.ok().body(new APIResponse<>(patient));
  }

  /**
   * Creates a new patient.
   *
   * @param patientDTO the patient data transfer object containing patient details
   * @return ResponseEntity with APIResponse containing the created patient
   */
  @PostMapping
  public ResponseEntity<APIResponse<PatientDTO>> createPatient(@Validated({Default.class,
      CreatePatientValidationGroup.class}) @RequestBody final PatientDTO patientDTO) {

    PatientDTO createdPatient = patientService.createPatient(patientDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(createdPatient));
  }

  /**
   * Updates an existing patient.
   *
   * @param id         the ID of the patient to update
   * @param patientDTO the patient data transfer object containing updated details
   * @return ResponseEntity with APIResponse containing the updated patient
   */
  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<PatientDTO>> updatePatient(@PathVariable("id") final UUID id,
      @Validated({Default.class,
          CreatePatientValidationGroup.class}) @RequestBody final PatientDTO patientDTO) {

    PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
    return ResponseEntity.accepted().body(new APIResponse<>(updatedPatient));
  }

  /**
   * Deletes a patient by ID.
   *
   * @param id the ID of the patient to delete
   * @return ResponseEntity with APIResponse indicating success or failure
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<Void>> deletePatient(@PathVariable("id") final UUID id) {
    patientService.deletePatient(id);
    return ResponseEntity.accepted().body(new APIResponse<>(null));
  }

  /**
   * Deletes a patient by email.
   *
   * @param email the email of the patient to delete
   * @return ResponseEntity with APIResponse indicating success or failure
   */
  @DeleteMapping("/email/{email}")
  public ResponseEntity<APIResponse<Void>> deletePatientByEmail(
      @Email @Validated @PathVariable("email") final String email) {
    patientService.deletePatientByEmail(email);
    return ResponseEntity.accepted().body(new APIResponse<>(null));
  }

}
