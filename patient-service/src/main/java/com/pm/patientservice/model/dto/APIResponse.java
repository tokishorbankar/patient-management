package com.pm.patientservice.model.dto;

public record APIResponse<T>(T data, String message, boolean success) {

  public static final String DEFAULT_SUCCESS_MESSAGE = "Operation successful";
  public static final String DEFAULT_ERROR_MESSAGE = "An error occurred";
  public static final boolean DEFAULT_SUCCESS_STATUS = true;
  public static final boolean DEFAULT_ERROR_STATUS = false;

  /**
   * Constructs an APIResponse with the provided data, message, and success status.
   *
   * @param data will be set to null if not provided for the default constructor
   * @return an APIResponse with the provided data, message, and success status as default values
   */
  public APIResponse(T data) {
    this(data, DEFAULT_SUCCESS_MESSAGE, DEFAULT_SUCCESS_STATUS);
  }

  /**
   * Constructs an APIResponse with the provided message and success status.
   *
   * @param data
   * @param message
   * @param success
   * @return an APIResponse with the provided data, message, and success status
   */
  public APIResponse(T data, String message, boolean success) {
    this.data = data;
    this.message = message;
    this.success = success;
  }

}
