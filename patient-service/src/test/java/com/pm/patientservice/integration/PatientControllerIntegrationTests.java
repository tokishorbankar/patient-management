package com.pm.patientservice.integration;

import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID;
import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_ID;
import static com.pm.patientservice.utility.UtilityService.buildRandomPatientDTO;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.pm.patientservice.configuration.TestContainersConfiguration;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.utility.UtilityService;
import io.restassured.RestAssured;
import java.util.List;
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
class PatientControllerIntegrationTests {

  @LocalServerPort
  int port;

  @BeforeEach
  void setUpBaseUrls() {
    String url = "http://localhost:%s/patients";
    RestAssured.baseURI = String.format(url, port);
  }

  // Helper method to create and persist a patient, returning the id
  private String createPatientAndGetId(PatientDTO dto) {
    return RestAssured
        .given()
        .contentType("application/json")
        .body(dto)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("data.email", equalTo(dto.getEmail()))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true))
        .extract()
        .path("data.id");
  }

  // Helper method to create and persist a patient, returning the email
  private String createPatientAndGetEmail(PatientDTO dto) {
    return RestAssured
        .given()
        .contentType("application/json")
        .body(dto)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("data.email", equalTo(dto.getEmail()))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true))
        .extract()
        .path("data.email");
  }

  @Test
  @DisplayName("should return all patients")
  void should_Returns_AllPatients() {

    List<String> ids = Stream
        .generate(UtilityService::buildRandomPatientDTO)
        .limit(5)
        .map(this::createPatientAndGetId)
        .toList();

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
    PatientDTO dto = buildRandomPatientDTO();
    String expectedId = createPatientAndGetId(dto);

    RestAssured
        .given()
        .when()
        .get("/{id}", expectedId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("data.id", equalTo(expectedId))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return patient by email")
  void should_ReturnPatientByEmail() {
    PatientDTO dto = buildRandomPatientDTO();
    String expectedEmailId = createPatientAndGetEmail(dto);
    RestAssured
        .given()
        .when()
        .get("/email/{email}", expectedEmailId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("data.email", equalTo(expectedEmailId))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should create a new patient")
  void should_CreateNewPatient() {
    PatientDTO patientDTO = buildRandomPatientDTO();
    createPatientAndGetId(patientDTO);
  }

  @Test
  @DisplayName("should update an existing patient")
  void should_UpdateExistingPatient() {
    PatientDTO dto = buildRandomPatientDTO();
    String id = createPatientAndGetId(dto);

    String expectedEmailId = dto.getEmail();

    String actualEmail = Instancio.gen().net().email().toString();
    dto.setEmail(actualEmail);

    RestAssured
        .given()
        .contentType("application/json")
        .body(dto)
        .when()
        .put("/{id}", id)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("data.id", equalTo(id))
        .body("data.email", not(equalTo(expectedEmailId)))
        .body("data.email", equalTo(actualEmail))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should delete a patient by ID")
  void should_DeletePatientById() {
    PatientDTO dto = buildRandomPatientDTO();
    String id = createPatientAndGetId(dto);

    RestAssured
        .given()
        .when()
        .delete("/{id}", id)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should delete a patient by email")
  void should_DeletePatientByEmail() {
    PatientDTO dto = buildRandomPatientDTO();
    String expectedEmailId = createPatientAndGetEmail(dto);

    RestAssured
        .given()
        .when()
        .delete("/email/{email}", expectedEmailId)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return 404 when patient not found by ID")
  void should_Return404WhenPatientNotFoundById() {
    PatientDTO dto = buildRandomPatientDTO();
    String expectedId = createPatientAndGetId(dto);

    RestAssured
        .given()
        .when()
        .delete("/{id}", expectedId)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));

    RestAssured
        .given()
        .when()
        .get("/{id}", expectedId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, expectedId)))
        .body("success", equalTo(false));
  }

  @Test
  @DisplayName("should return 404 when patient not found by email")
  void should_Return404WhenPatientNotFoundByEmail() {
    PatientDTO dto = buildRandomPatientDTO();
    String expectedEmailId = createPatientAndGetEmail(dto);

    RestAssured
        .given()
        .when()
        .delete("/email/{email}", expectedEmailId)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));

    RestAssured
        .given()
        .when()
        .get("/email/{email}", expectedEmailId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message",
            equalTo(String.format(ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID, expectedEmailId)))
        .body("success", equalTo(false));
  }

}
