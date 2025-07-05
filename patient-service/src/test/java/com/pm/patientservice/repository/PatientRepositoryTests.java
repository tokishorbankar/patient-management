package com.pm.patientservice.repository;

import com.pm.patientservice.configuration.TestContainersConfiguration;
import com.pm.patientservice.model.entities.Patient;
import com.pm.patientservice.utility.UtilityService;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(classes = TestContainersConfiguration.class)
@DataJpaTest
public class PatientRepositoryTests {

  @Autowired
  private PatientRepository patientRepository;

  @Test
  @DisplayName("Test to find a patient by email")
  public void testFindByEmail() {
    Patient patient = UtilityService.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    var foundPatient = patientRepository.findByEmail(savedPatient.getEmail());

    assert foundPatient.isPresent();
    assert foundPatient.get().getEmail().equals(savedPatient.getEmail());
  }

  @Test
  @DisplayName("Test to check if a patient not exists by email")
  public void testNotExistsByEmail() {
    String randomEmail = Instancio.gen().net().email().toString();

    var foundPatient = patientRepository.findByEmail(randomEmail);
    assert foundPatient.isEmpty();
  }

  @Test
  @DisplayName("Test to find a patient by ID")
  public void testFindById() {
    Patient patient = UtilityService.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    var foundPatient = patientRepository.findById(savedPatient.getId());

    assert foundPatient.isPresent();
    assert foundPatient.get().getId().equals(savedPatient.getId());
  }

  @Test
  @DisplayName("Test to check if a patient not exists by ID")
  public void testNotExistsById() {
    var foundPatient = patientRepository.findById(Instancio.create(UUID.class));
    assert foundPatient.isEmpty();
  }

  @Test
  @DisplayName("Test to check if a patient exists by email")
  public void testExistsByEmail() {
    Patient patient = UtilityService.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    boolean exists = patientRepository.existsByEmail(savedPatient.getEmail());

    assert exists;
  }


  @Test
  @DisplayName("Test to check if a patient exists by email and ID not matching")
  public void testExistsByEmailAndIdNot() {
    Patient patient = UtilityService.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    boolean exists = patientRepository.existsByEmailAndIdNot(savedPatient.getEmail(),
        UUID.randomUUID());

    assert exists;
  }

  @Test
  @DisplayName("Test to check if a patient exists by email and ID matching")
  public void testExistsByEmailAndId() {
    Patient patient = UtilityService.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    boolean exists = patientRepository.existsByEmailAndIdNot(savedPatient.getEmail(),
        savedPatient.getId());

    assert !exists;
  }

}