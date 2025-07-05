package com.pm.patientservice.utility;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.model.entities.Patient;
import org.instancio.Instancio;

public final class UtilityService {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .registerModule(new JavaTimeModule())
      .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
      .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
      .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
      .configure(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE, true);

  private UtilityService() {
    // Utility class, no instantiation allowed
  }


  /**
   * Builds a random PatientDTO object with generated data.
   *
   * @return a PatientDTO object with random data
   * @see PatientDTO
   * @see Instancio
   * @see <a href="https://instancio.org/">Instancio documentation</a>
   */
  // Helper method to create a PatientDTO with random data
  public static PatientDTO buildRandomPatientDTO() {
    return Instancio.of(PatientDTO.class)
        .ignore(field(PatientDTO::getId))
        .generate(field(PatientDTO::getRegisteredDate), gen -> gen.temporal().localDate().past())
        .generate(field(PatientDTO::getDateOfBirth), gen -> gen.temporal().localDate().past())
        .generate(field(PatientDTO::getEmail), gen -> gen.net().email())
        .create();
  }


  /**
   * Builds a random Patient object with generated data.
   *
   * @return a Patient object with random data
   * @see Patient
   * @see Instancio
   * @see <a href="https://instancio.org/">Instancio documentation</a>
   */
  // Helper method to create a Patient with random data
  public static Patient buildRandomPatient() {
    return Instancio.of(Patient.class)
        .generate(field(Patient::getEmail), gen -> gen.net().email())
        .generate(field(Patient::getRegisteredDate), gen -> gen.temporal().localDate().past())
        .generate(field(Patient::getDateOfBirth), gen -> gen.temporal().localDate().past())
        .create();
  }


  /**
   * Converts an object of type T to a JSON string.
   *
   * @param object the object to convert
   * @param <T>    the type of the object
   * @return a JSON string representation of the object
   */
  public static <T> String convertObjectToJsonString(T object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting object to JSON string", e);
    }
  }

  /**
   * Converts a JSON string to an object of type T.
   *
   * @param jsonString the JSON string to convert
   * @param clazz      the class of the object type
   * @param <T>        the type of the object
   * @return an instance of T with data from the JSON string
   */
  public static <T> T convertJsonStringToObject(String jsonString, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting JSON string to object", e);
    }
  }


  /**
   * Converts an object of type T to an object of type R using JSON serialization and
   * deserialization.
   *
   * @param object      the object to convert
   * @param targetClass the class of the target object type
   * @param <R>         the type of the target object
   * @param <T>         the type of the source object
   * @return an instance of R with the data from the source object
   */
  public static <R, T> R convertObjectToObject(T object, Class<R> targetClass) {
    try {
      String jsonString = OBJECT_MAPPER.writeValueAsString(object);
      return OBJECT_MAPPER.readValue(jsonString, targetClass);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting object to object", e);
    }
  }

  /**
   * Asserts that two objects of type T are deeply equal by comparing their JSON string
   * representations.
   *
   * @param expected the expected object
   * @param actual   the actual object
   * @param <T>      the type of the objects
   */
  public static <T> void assertDeepObjectEquals(T expected, T actual) {
    if (expected == null && actual == null) {
      return;
    }
    if (expected == null || actual == null) {
      throw new AssertionError("One of the objects is null, but not both");
    }
    String expectedJson = convertObjectToJsonString(expected);
    String actualJson = convertObjectToJsonString(actual);
    assertEquals(expectedJson, actualJson,
        "Objects are not equal. Expected: " + expectedJson + ", Actual: " + actualJson);
  }

}
