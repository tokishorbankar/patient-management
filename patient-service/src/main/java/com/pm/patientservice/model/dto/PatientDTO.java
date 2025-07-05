package com.pm.patientservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pm.patientservice.model.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class PatientDTO {

  private UUID id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotNull(message = "Date of birth is required")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @PastOrPresent(message = "Date of birth must be in the past or present")
  private LocalDate dateOfBirth;

  @NotBlank(message = "Address is required")
  private String address;

  @NotNull(message = "Registered date is required", groups = CreatePatientValidationGroup.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @PastOrPresent(message = "Registered date must be in the past or present")
  private LocalDate registeredDate;

  public PatientDTO() {
    super();
  }

}
