package com.pm.patientservice.exception.advice;

import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pm.patientservice.controller.PatientController;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.utility.UtilityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PatientController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PatientController patientController;

  @Test
  @DisplayName("should return 400 when invalid argument is provided")
  void shouldReturn400WhenInvalidArgumentProvided() throws Exception {
    PatientDTO dto = UtilityService.buildRandomPatientDTO();
    dto.setEmail("invalid-email-format");
    dto.setName("");

    mockMvc.perform(post("/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(UtilityService.convertObjectToJsonString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("An error occurred"))
        .andExpect(jsonPath("$.data.email").value("Email should be valid"))
        .andExpect(jsonPath("$.data.name").value("Name is required"));

  }

  @Test
  @DisplayName("should return 404 when patient already exists")
  void shouldReturn404WhenPatientAlreadyExists() throws Exception {
    String email = "tester@example.com";

    when(patientController.getPatientByEmail(email))
        .thenThrow(new PatientNotFoundException(
            String.format(ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID, email)));

    mockMvc.perform(get("/patients/email/{email}", email))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value(
            String.format(ERROR_MESSAGE_ALREADY_EXISTS_BY_EMAIL_ID, email)));
  }

}
