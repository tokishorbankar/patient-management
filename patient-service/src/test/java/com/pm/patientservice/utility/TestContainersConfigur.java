package com.pm.patientservice.utility;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfigur {

  private final static String POSTGRES_IMAGE = "postgres:17.0";
  private final static String POSTGRES_DB = "patienttestdb";
  private final static String POSTGRES_USER = "tester";
  private final static String POSTGRES_PASSWORD = "tester";
  private final static int POSTGRES_PORT = 5432;

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
