package com.pm.patientservice.controller;

import com.pm.patientservice.utility.TestContainersConfigur;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfigur.class)
@Testcontainers
class PatientControllerIntegrationTest {

  private String BASE_URL_PATIENTS = "http://localhost:%s/patients";

  @LocalServerPort
  int port;

  @BeforeEach
  void setUpBaseUrls() {
    RestAssured.baseURI = String.format(BASE_URL_PATIENTS, port);
  }

  @Test
  @DisplayName("should return all patients")
  void should_Returns_AllPatients() {
    RestAssured.given()
        .when()
        .request(Method.GET, "")
        .then()
        .statusCode(200)
        .body("size()", org.hamcrest.Matchers.greaterThan(0))
        .body("message",
            org.hamcrest.Matchers.equalTo("Operation successful"))
        .body("success", org.hamcrest.Matchers.equalTo(true));
  }

  @Test
  @DisplayName("should return patient by ID")
  void should_ReturnPatientById() {
    RestAssured.given()
        .when()
        .request(Method.GET, "/{id}", "123e4567-e89b-12d3-a456-426614174000")
        .then()
        .statusCode(200)
        .body("data.id", org.hamcrest.Matchers.equalTo("123e4567-e89b-12d3-a456-426614174000"))
        .body("message", org.hamcrest.Matchers.equalTo("Operation successful"))
        .body("success", org.hamcrest.Matchers.equalTo(true));
  }
}
