package com.pm.patientservice.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patient")
@Builder
@Setter
@Getter
@AllArgsConstructor

public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  private String name;

  @NotNull
  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @NotNull
  @PastOrPresent
  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @NotNull
  private String address;

  @NotNull
  @PastOrPresent
  @Column(name = "registered_date")
  private LocalDate registeredDate;

  public Patient() {
  }

}
