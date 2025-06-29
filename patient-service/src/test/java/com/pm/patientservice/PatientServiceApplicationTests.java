package com.pm.patientservice;

import com.pm.patientservice.utility.TestContainersConfigur;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = TestContainersConfigur.class)
class PatientServiceApplicationTests {

  @Test
  @DisplayName("should load application context successfully")
  void contextLoads() {
    // This test will pass if the application context loads successfully
  }
}
