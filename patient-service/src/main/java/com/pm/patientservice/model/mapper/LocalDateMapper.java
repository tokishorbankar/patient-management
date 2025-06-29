package com.pm.patientservice.model.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
public class LocalDateMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Converts a LocalDate to a String in the format "yyyy-MM-dd".
   *
   * @param date the LocalDate to convert
   * @return the formatted date as a String
   */
  public String asString(LocalDate date) {
    return date != null ? date.format(DATE_FORMATTER) : null;
  }

  /**
   * Converts a String in the format "yyyy-MM-dd" to a LocalDate.
   *
   * @param date the String to convert
   * @return the LocalDate representation of the String
   */
  public LocalDate asLocalDate(String date) {
    try {
      return date != null ? LocalDate.parse(date, DATE_FORMATTER) : null;
    } catch (DateTimeParseException exception) {
      throw new IllegalArgumentException("Invalid date format, expected 'yyyy-MM-dd': " + date,
          exception);
    }
  }
}
