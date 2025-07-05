package com.pm.patientservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import com.pm.patientservice.configuration.TestContainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = TestContainersConfiguration.class)
class PatientServiceApplicationTests {

  @Test
  @DisplayName("should load application context successfully")
  void contextLoads() {
    // This test will pass if the application context loads successfully
  }


  @Test
  @DisplayName("should run main method with args without exceptions")
  void mainMethodWithArgsRunsWithoutException() {
    try (MockedStatic<PatientServiceApplication> mockedStatic = org.mockito.Mockito.mockStatic(
        PatientServiceApplication.class)) {
      mockedStatic.when(() -> PatientServiceApplication.main(new String[]{"arg1", "arg2"}))
          .thenCallRealMethod();
      assertDoesNotThrow(() ->
          PatientServiceApplication.main(new String[]{"arg1", "arg2"})
      );

      mockedStatic.verify(() -> PatientServiceApplication.main(new String[]{"arg1", "arg2"}));
      mockedStatic.verify(() -> PatientServiceApplication.main(any(String[].class)), times(1));
    }

  }

}
