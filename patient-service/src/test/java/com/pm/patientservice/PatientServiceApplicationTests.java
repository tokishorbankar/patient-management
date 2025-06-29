package com.pm.patientservice;

import com.pm.patientservice.utility.TestContainersConfigur;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestContainersConfigur.class)
class PatientServiceApplicationTests {

  @Test
  void contextLoads() {
    // Test will pass if the application context loads successfully
  }

}
