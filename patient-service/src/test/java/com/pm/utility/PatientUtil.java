package com.pm.utility;

import static org.instancio.Select.field;

import com.pm.patientservice.model.dto.PatientDTO;
import org.instancio.Instancio;

public final class PatientUtil {

  private PatientUtil() {
    // Utility class, no instantiation allowed
  }

  // Helper method to create a PatientDTO with random data
  public static PatientDTO buildRandomPatientDTO() {
    return Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .set(field(PatientDTO::getRegisteredDate), "2023-12-01")
        .set(field(PatientDTO::getDateOfBirth), "2020-12-01")
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();
  }


}
