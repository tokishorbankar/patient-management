package com.pm.patientservice.controller;

import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID;
import static com.pm.patientservice.service.PatientService.ERROR_MESSAGE_NOT_FOUND_BY_ID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.instancio.Select.field;

import com.pm.patientservice.configuration.TestContainersConfiguration;
import com.pm.patientservice.model.dto.PatientDTO;
import io.restassured.RestAssured;
import java.util.UUID;
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

  private String BASE_URL_PATIENTS = "http://localhost:%s/patients";

  @BeforeEach
  void setUpBaseUrls() {
    RestAssured.baseURI = String.format(BASE_URL_PATIENTS, port);
  }

  @Test
  @DisplayName("should return all patients")
  void should_Returns_AllPatients() {
    RestAssured
        .given()
        .when()
        .get("")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("size()", org.hamcrest.Matchers.greaterThan(0))
        .body("message",
            equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return patient by ID")
  void should_ReturnPatientById() {
    PatientDTO patientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    String id = RestAssured
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
    PatientDTO patientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    String email = RestAssured
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
    PatientDTO patientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    RestAssured
        .given()
        .contentType("application/json")
        .body(patientDTO)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("data.email", equalTo(patientDTO.getEmail()))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should update an existing patient")
  void should_UpdateExistingPatient() {
    PatientDTO savePatientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    String idAsString =
        RestAssured
            .given()
            .contentType("application/json")
            .body(savePatientDTO)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("data.email", equalTo(savePatientDTO.getEmail()))
            .body("message", equalTo("Operation successful"))
            .body("success", equalTo(true))
            .extract()
            .path("data.id");

    UUID alreadyExistingPatientId = UUID.fromString(idAsString);

    String originalEmail = savePatientDTO.getEmail();
    String newEmail = (String.valueOf(Instancio.gen().net().email()));
    savePatientDTO.setEmail(newEmail);

    RestAssured
        .given()
        .contentType("application/json")
        .body(savePatientDTO)
        .when()
        .put("/{id}", alreadyExistingPatientId)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("data.id", equalTo(alreadyExistingPatientId.toString()))
        .body("data.email", not(equalTo(originalEmail)))
        .body("data.email", equalTo(newEmail))
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should delete a patient by ID")
  void should_DeletePatientById() {
    String idAsString =
        RestAssured
            .given()
            .when()
            .get("/{id}", "123e4567-e89b-12d3-a456-426614174000")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("data.id", equalTo("123e4567-e89b-12d3-a456-426614174000"))
            .body("message", equalTo("Operation successful"))
            .body("success", equalTo(true))
            .extract()
            .path("data.id");

    UUID patientId = UUID.fromString(idAsString);

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
    PatientDTO savePatientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    String emailIddAsString =
        RestAssured
            .given()
            .contentType("application/json")
            .body(savePatientDTO)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("data.email", equalTo(savePatientDTO.getEmail()))
            .body("message", equalTo("Operation successful"))
            .body("success", equalTo(true))
            .extract()
            .path("data.email");

    RestAssured
        .given()
        .when()
        .delete("/email/{email}", emailIddAsString)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));
  }

  @Test
  @DisplayName("should return 404 when patient not found by ID")
  void should_Return404WhenPatientNotFoundById() {
    PatientDTO savePatientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    String iddAsString =
        RestAssured
            .given()
            .contentType("application/json")
            .body(savePatientDTO)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("data.email", equalTo(savePatientDTO.getEmail()))
            .body("message", equalTo("Operation successful"))
            .body("success", equalTo(true))
            .extract()
            .path("data.id");

    RestAssured
        .given()
        .when()
        .delete("/{id}", iddAsString)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));

    RestAssured
        .given()
        .when()
        .get("/{id}", iddAsString)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ID, iddAsString)))
        .body("success", equalTo(false));
  }

  @Test
  @DisplayName("should return 404 when patient not found by email")
  void should_Return404WhenPatientNotFoundByEmail() {
    PatientDTO savePatientDTO = Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();

    String emailIdAsString =
        RestAssured
            .given()
            .contentType("application/json")
            .body(savePatientDTO)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("data.email", equalTo(savePatientDTO.getEmail()))
            .body("message", equalTo("Operation successful"))
            .body("success", equalTo(true))
            .extract()
            .path("data.email");

    RestAssured
        .given()
        .when()
        .delete("/email/{email}", emailIdAsString)
        .then()
        .statusCode(HttpStatus.ACCEPTED.value())
        .body("message", equalTo("Operation successful"))
        .body("success", equalTo(true));

    RestAssured
        .given()
        .when()
        .get("/email/{email}", emailIdAsString)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message",
            equalTo(String.format(ERROR_MESSAGE_NOT_FOUND_BY_EMAIL_ID, emailIdAsString)))
        .body("success", equalTo(false));
  }


}
