package com.pm.patientservice.repository;

import com.pm.patientservice.model.entities.Patient;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByEmail(final String email);

    Optional<Patient> findById(final UUID id);

    boolean existsByEmail(final String email);

    boolean existsByEmailAndIdNot(final String email, final UUID id);
}
