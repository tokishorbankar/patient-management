package com.pm.patientservice.exception;

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(final String message) {
    super(message);
  }

  public EmailAlreadyExistsException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
