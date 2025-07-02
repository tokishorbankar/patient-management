package com.pm.patientservice.repository;

import com.pm.patientservice.configuration.TestContainersConfiguration;
import com.pm.patientservice.model.entities.Patient;
import com.pm.patientservice.utility.PatientUtil;
import java.util.UUID;
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
    Patient patient = PatientUtil.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    var foundPatient = patientRepository.findByEmail(savedPatient.getEmail());

    assert foundPatient.isPresent();
    assert foundPatient.get().getEmail().equals(savedPatient.getEmail());
    assert patientRepository.existsByEmail(savedPatient.getEmail());
  }

  @Test
  @DisplayName("Test to find a patient by ID")
  public void testFindById() {
    Patient patient = PatientUtil.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    var foundPatient = patientRepository.findById(savedPatient.getId());

    assert foundPatient.isPresent();
    assert foundPatient.get().getId().equals(savedPatient.getId());
    assert patientRepository.existsById(savedPatient.getId());
  }

  @Test
  @DisplayName("Test to check if a patient exists by email")
  public void testExistsByEmail() {
    Patient patient = PatientUtil.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    boolean exists = patientRepository.existsByEmail(savedPatient.getEmail());

    assert exists;
  }

  @Test
  @DisplayName("Test to check if a patient exists by email and ID not matching")
  public void testExistsByEmailAndIdNot() {
    Patient patient = PatientUtil.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    boolean exists = patientRepository.existsByEmailAndIdNot(savedPatient.getEmail(),
        savedPatient.getId());

    assert !exists;
  }

  @Test
  @DisplayName("Test to check if a patient exists by email and ID matching")
  public void testExistsByEmailAndId() {
    Patient patient = PatientUtil.buildRandomPatient();
    patient.setId(null);
    Patient savedPatient = patientRepository.save(patient);

    boolean exists = patientRepository.existsByEmailAndIdNot(savedPatient.getEmail(),
        savedPatient.getId());

    assert !exists;
  }


}