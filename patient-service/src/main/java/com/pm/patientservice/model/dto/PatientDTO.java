package com.pm.patientservice.model.dto;

import com.pm.patientservice.model.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

  private UUID id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Date of birth is required")
  private String dateOfBirth;

  @NotBlank(message = "Address is required")
  private String address;

  @NotBlank(message = "Registered date is required", groups = CreatePatientValidationGroup.class)
  private String registeredDate;

}
