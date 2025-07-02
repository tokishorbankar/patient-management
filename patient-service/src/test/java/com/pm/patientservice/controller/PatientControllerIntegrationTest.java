package com.pm.patientservice.controller;

import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID;
import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_ID;
import static com.pm.utility.PatientUtil.buildRandomPatientDTO;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.pm.patientservice.configuration.TestContainersConfiguration;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.utility.PatientUtil;
import io.restassured.RestAssured;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfiguration.class)
@Testcontainers
class PatientControllerIntegrationTest {

  @LocalServerPort
  int port;

  @BeforeEach
  void setUpBaseUrls() {
    String url = "http://localhost:%s/patients";
    RestAssured.baseURI = String.format(url, port);
  }

  // Helper method to create and persist a patient, returning the id
  private String createPatientAndGetId(PatientDTO patientDTO) {
    return RestAssured
        .given()
        .contentType("application/json")
        .body(patientDTO)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("data.email", equalTo(patientDTO.getEmail()))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true))
        .extract()
        .path("data.id");
  }

  // Helper method to create and persist a patient, returning the email
  private String createPatientAndGetEmail(PatientDTO patientDTO) {
    return RestAssured
        .given()
        .contentType("application/json")
        .body(patientDTO)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("data.email", equalTo(patientDTO.getEmail()))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true))
        .extract()
        .path("data.email");
  }

  @Test
  @DisplayName("should return all patients")
  void should_Returns_AllPatients() {
    // crate list of patientDTOSList using buildRandomPatientDTO method use java stream

    List<PatientDTO> patientDTOList = Stream.generate(PatientUtil::buildRandomPatientDTO)
        .limit(5)
        .toList();

    List<String> ids = patientDTOList.stream().map(this::createPatientAndGetId).toList();

    RestAssured
        .given()
        .when()
        .get("")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("size()", greaterThan(0))
        .body("data.id", hasItems(ids.toArray()))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return patient by ID")
  void should_ReturnPatientById() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    String id = createPatientAndGetId(patientDTO);
    RestAssured
        .given()
        .when()
        .get("/{id}", id)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("data.id", equalTo(id))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return patient by email")
  void should_ReturnPatientByEmail() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    String email = createPatientAndGetEmail(patientDTO);
    RestAssured
        .given()
        .when()
        .get("/email/{email}", email)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("data.email", equalTo(email))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should create a new patient")
  void should_CreateNewPatient() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    createPatientAndGetId(patientDTO); // assertion is in helper
  }

  @Test
  @DisplayName("should update an existing patient")
  void should_UpdateExistingPatient() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    String id = createPatientAndGetId(patientDTO);

    UUID patientId = UUID.fromString(id);

    String originalEmail = patientDTO.getEmail();

    String newEmail = Instancio.gen().net().email().toString();
    patientDTO.setEmail(newEmail);

    RestAssured
        .given()
        .contentType("application/json")
        .body(patientDTO)
        .when()
        .put("/{id}", patientId)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("data.id", equalTo(patientId.toString()))
        .body("data.email", not(equalTo(originalEmail)))
        .body("data.email", equalTo(newEmail))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should delete a patient by ID")
  void should_DeletePatientById() {
    // For a real test, create a patient first, then delete
    PatientDTO patientDTO = buildRandomPatientDTO();
    String id = createPatientAndGetId(patientDTO);

    UUID patientId = UUID.fromString(id);

    RestAssured
        .given()
        .when()
        .delete("/{id}", patientId)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should delete a patient by email")
  void should_DeletePatientByEmail() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    String email = createPatientAndGetEmail(patientDTO);

    RestAssured
        .given()
        .when()
        .delete("/email/{email}", email)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return 404 when patient not found by ID")
  void should_Return404WhenPatientNotFoundById() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    String id = createPatientAndGetId(patientDTO);

    RestAssured
        .given()
        .when()
        .delete("/{id}", id)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));

    RestAssured
        .given()
        .when()
        .get("/{id}", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, id)))
        .body("success", equalTo(false));
  }

  @Test
  @DisplayName("should return 404 when patient not found by email")
  void should_Return404WhenPatientNotFoundByEmail() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    String email = createPatientAndGetEmail(patientDTO);

    RestAssured
        .given()
        .when()
        .delete("/email/{email}", email)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));

    RestAssured
        .given()
        .when()
        .get("/email/{email}", email)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo(String.format(ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID, email)))
        .body("success", equalTo(false));
  }

}
