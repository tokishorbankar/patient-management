package com.pm.patientservice.controller;

import com.pm.patientservice.model.dto.APIResponse;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.model.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Tag(name = "Patients", description = "API for managing Patients")
public class PatientController {

  private static final Logger log = LoggerFactory.getLogger(PatientController.class);

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
  @Operation(summary = "Get all patients",
      description = "Retrieves a list of all patients in the system.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved all patients",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      }
  )
  public ResponseEntity<APIResponse<List<PatientDTO>>> getAllPatients() {
    log.info("Retrieving all patients");
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
  @Operation(summary = "Get patient by ID",
      description = "Retrieves a patient by their unique identifier.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved patient by ID",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "404",
              description = "Patient not found",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      })
  public ResponseEntity<APIResponse<PatientDTO>> getPatientById(@PathVariable("id") final UUID id) {
    log.info("Retrieving patient with ID: {}", id);
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
  @Operation(summary = "Get patient by email",
      description = "Retrieves a patient by their email address.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved patient by email",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "404",
              description = "Patient not found",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      }
  )
  public ResponseEntity<APIResponse<PatientDTO>> getPatientByEmail(
      @PathVariable("email") final String email) {
    log.info("Retrieving patient with email: {}", email);
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
  @Operation(summary = "Create a new patient",
      description = "Creates a new patient in the system with the provided details.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "201",
              description = "Successfully created patient",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "400",
              description = "Bad request due to validation errors",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      }
  )
  public ResponseEntity<APIResponse<PatientDTO>> createPatient(@Validated({Default.class,
      CreatePatientValidationGroup.class}) @RequestBody final PatientDTO patientDTO) {
    log.info("Creating new patient with DTO: {}", patientDTO);
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
  @Operation(summary = "Update patient",
      description = "Updates an existing patient's details by their unique identifier.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "202",
              description = "Successfully updated patient",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "404",
              description = "Patient not found",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      })
  public ResponseEntity<APIResponse<PatientDTO>> updatePatient(@PathVariable("id") final UUID id,
      @Validated({Default.class,
          CreatePatientValidationGroup.class}) @RequestBody final PatientDTO patientDTO) {
    log.info("Updating patient with ID: {} and DTO: {}", id, patientDTO);

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
  @Operation(summary = "Delete patient by ID",
      description = "Deletes a patient from the system by their unique identifier.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "202",
              description = "Successfully deleted patient",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "404",
              description = "Patient not found",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      }
  )
  public ResponseEntity<APIResponse<Void>> deletePatient(@PathVariable("id") final UUID id) {
    patientService.deletePatient(id);
    log.info("Deleted patient with ID: {}", id);
    return ResponseEntity.accepted().body(new APIResponse<>(null));
  }

  /**
   * Deletes a patient by email.
   *
   * @param email the email of the patient to delete
   * @return ResponseEntity with APIResponse indicating success or failure
   */
  @DeleteMapping("/email/{email}")
  @Operation(summary = "Delete patient by email",
      description = "Deletes a patient from the system by their email address.",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "202",
              description = "Successfully deleted patient by email",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = "404",
              description = "Patient not found",
              content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
              )
          )
      }
  )
  public ResponseEntity<APIResponse<Void>> deletePatientByEmail(
      @Email @Validated @PathVariable("email") final String email) {
    log.info("Deleting patient with email: {}", email);
    patientService.deletePatientByEmail(email);
    return ResponseEntity.accepted().body(new APIResponse<>(null));
  }

}
