package com.pm.patientservice.exception;

public class PatientNotFoundException extends RuntimeException {

  public PatientNotFoundException(final String message) {
    super(message);
  }

  public PatientNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
