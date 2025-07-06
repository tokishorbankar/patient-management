package com.pm.patientservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

  /**
   * Configures the OpenAPI documentation for the Patient Service API.
   *
   * @return OpenAPI instance with API metadata
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new io.swagger.v3.oas.models.info.Info()
            .title("Patient Service API")
            .version("1.0.0")
            .description("API for managing patient data in the healthcare system."));
  }
}
