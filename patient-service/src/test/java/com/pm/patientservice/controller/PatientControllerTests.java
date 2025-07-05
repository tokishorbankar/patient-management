package com.pm.patientservice.controller;

import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID;
import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_ID;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.service.PatientService;
import com.pm.patientservice.utility.UtilityService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PatientController.class)
class PatientControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PatientService patientService;


  @Test
  @DisplayName("should return all patients when getAllPatients is called")
  void shouldReturnAllPatientsWhenGetAllPatientsIsCalled() throws Exception {
    List<PatientDTO> expectedDTOs = Stream.generate(UtilityService::buildRandomPatientDTO)
        .limit(5)
        .collect(Collectors.toList());

    when(patientService.getAllPatients()).thenReturn(expectedDTOs);

    mockMvc.perform(get("/patients"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(5)))
        .andExpect(jsonPath("$.message").value("Operation successful"))
        .andExpect(jsonPath("$.success").value(true));

    verify(patientService, times(1)).getAllPatients();
  }

  @Test
  @DisplayName("should return empty list when no patients exist")
  void shouldReturnEmptyListWhenNoPatientsExist() throws Exception {
    when(patientService.getAllPatients()).thenReturn(List.of());

    mockMvc.perform(get("/patients"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(0)))
        .andExpect(jsonPath("$.message").value("Operation successful"))
        .andExpect(jsonPath("$.success").value(true));
    verify(patientService, times(1)).getAllPatients();
  }

  @Test
  @DisplayName("should return patient by id")
  void shouldReturnPatientById() throws Exception {
    UUID id = UUID.randomUUID();
    PatientDTO patientDTO = UtilityService.buildRandomPatientDTO();
    patientDTO.setId(id);

    when(patientService.getPatientById(id)).thenReturn(patientDTO);

    mockMvc.perform(get("/patients/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(id.toString()))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Operation successful"));
  }

  @Test
  @DisplayName("should return 404 when patient not found by id")
  void shouldReturn404WhenPatientNotFoundById() throws Exception {
    UUID id = UUID.randomUUID();

    when(patientService.getPatientById(id)).thenThrow(
        new PatientNotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, id)));

    mockMvc.perform(get("/patients/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, id)));

    verify(patientService, times(1)).getPatientById(id);
  }

  @Test
  @DisplayName("should return patient by email")
  void shouldReturnPatientByEmail() throws Exception {
    PatientDTO patientDTO = UtilityService.buildRandomPatientDTO();
    patientDTO.setId(UUID.randomUUID());

    String email = patientDTO.getEmail();

    when(patientService.getPatientByEmail(email)).thenReturn(patientDTO);

    mockMvc.perform(get("/patients/email/{email}", email))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.email").value(email))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Operation successful"));

    verify(patientService, times(1)).getPatientByEmail(email);
  }

  @Test
  @DisplayName("should return 404 when patient not found by email")
  void shouldReturn404WhenPatientNotFoundByEmail() throws Exception {
    String email = "tester@exaple.com";

    when(patientService.getPatientByEmail(email)).thenThrow(
        new PatientNotFoundException(
            String.format(ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID, email)));

    mockMvc.perform(get("/patients/email/{email}", email))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value(
            String.format(ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID, email)));

    verify(patientService, times(1)).getPatientByEmail(email);
  }


  @Test
  @DisplayName("should create a new patient")
  void shouldCreateNewPatient() throws Exception {
    PatientDTO patientDTO = UtilityService.buildRandomPatientDTO();
    UUID generatedId = UUID.randomUUID();
    patientDTO.setId(generatedId);

    when(patientService.createPatient(any(PatientDTO.class))).thenReturn(patientDTO);

    String requestBody = UtilityService.convertObjectToJsonString(patientDTO);

    mockMvc.perform(post("/patients")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Operation successful"));

    verify(patientService, times(1)).createPatient(any(PatientDTO.class));
  }

}