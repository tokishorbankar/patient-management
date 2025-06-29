package com.pm.patientservice.exception.advice;

import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.model.dto.APIResponse;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<APIResponse<Map<String, String>>> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException ex) {
    log.warn("Validation error: {}", ex.getMessage());

    Map<String, String> errors = (ex
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            DefaultMessageSourceResolvable::getDefaultMessage
        )));

    APIResponse<Map<String, String>> response = new APIResponse<>(errors,
        APIResponse.DEFAULT_ERROR_MESSAGE, APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<APIResponse<String>> handleEmailAlreadyExistsException(
      final EmailAlreadyExistsException ex) {
    log.warn("Email already exists: {}", ex.getMessage());

    APIResponse<String> response = new APIResponse<>(null, ex.getMessage(),
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.status(409).body(response);
  }

  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<APIResponse<String>> handlePatientNotFoundException(
      final PatientNotFoundException ex) {
    log.warn("Patient not found: {}", ex.getMessage());

    APIResponse<String> response = new APIResponse<>(null, ex.getMessage(),
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.status(404).body(response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<APIResponse<String>> handleNoResourceFoundException(
      final NoResourceFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());

    APIResponse<String> response = new APIResponse<>(null, ex.getMessage(),
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.status(404).body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<APIResponse<String>> handleMethodArgumentTypeMismatchException(
      final MethodArgumentTypeMismatchException ex) {
    log.warn("Method argument type mismatch: {}", ex.getMessage());

    APIResponse<String> response = new APIResponse<>(null,
        "Invalid argument type: " + ex.getMessage(),
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<APIResponse<String>> handleHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException ex) {
    log.warn("HTTP method not supported: {}", ex.getMessage());

    APIResponse<String> response = new APIResponse<>(null,
        "HTTP method not supported: " + ex.getMessage(),
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.status(405).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<APIResponse<String>> handleIllegalArgumentException(
      final IllegalArgumentException ex) {
    log.warn("Illegal argument: {}", ex.getMessage());

    APIResponse<String> response = new APIResponse<>(null,
        "Invalid argument: " + ex.getMessage(),
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponse<String>> handleGenericException(final Exception ex) {
    log.error("An unexpected error occurred: {}", ex.getMessage(), ex);

    APIResponse<String> response = new APIResponse<>(null,
        "An unexpected error occurred. Please try again later.",
        APIResponse.DEFAULT_ERROR_STATUS);

    return ResponseEntity.status(500).body(response);
  }

}
