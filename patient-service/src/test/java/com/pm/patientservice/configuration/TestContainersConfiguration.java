package com.pm.patientservice.configuration;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {

  private final String POSTGRES_IMAGE = "postgres:17.0";
  private final String POSTGRES_DB = "patienttestdb";
  private final String POSTGRES_USER = "tester";
  private final String POSTGRES_PASSWORD = "tester";
  private final int POSTGRES_PORT = 5432;

  @Bean
  @ServiceConnection
  PostgreSQLContainer<?> postgresContainer() {
    return new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
        .withDatabaseName(POSTGRES_DB)
        .withUsername(POSTGRES_USER)
        .withPassword(POSTGRES_PASSWORD)
        .withExposedPorts(POSTGRES_PORT);
  }


}
