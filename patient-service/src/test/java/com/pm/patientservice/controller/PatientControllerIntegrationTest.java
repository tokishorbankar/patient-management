package com.pm.patientservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.pm.patientservice.utility.TestContainersConfigur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfigur.class)
@Testcontainers
class PatientControllerIntegrationTest {

  @LocalServerPort
  int port;

  String baseUrl;

  @Autowired
  TestRestTemplate restTemplate;

  @BeforeEach
  void setUpBaseUrls() {
    baseUrl = "http://localhost:" + port + "/patients";
  }

  @Test
  void getAllPatientsWithValidRequest() {
    ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isNotNull();
  }
}
