package com.pm.patientservice.utility;

import java.lang.reflect.Array;
import org.instancio.Instancio;

public class RandomGenerateData {

  private RandomGenerateData() {
    // Private constructor to prevent instantiation
  }

  /**
   * Generates a random instance of the specified class.
   *
   * @param clazz the class to generate
   * @return a random instance of the specified class
   */
  public static <T> T generate(Class<T> clazz) {
    return Instancio.create(clazz);
  }

  /**
   * Generates a random instance of the specified class with a given seed.
   *
   * @param clazz the class to generate
   * @param seed  the seed for random generation
   * @return a random instance of the specified class
   */
  public static <T> T generate(Class<T> clazz, long seed) {
    return Instancio.of(clazz).withSeed(seed).create();
  }

  /**
   * Generates an array of random instances of the specified class.
   *
   * @param clazz the class to generate
   * @param seed  the seed for random generation
   * @param count the number of instances to generate
   * @return an array of random instances of the specified class
   */
  public static <T> T[] generate(Class<T> clazz, long seed, int count) {
    return Instancio.of(clazz)
        .withSeed(seed)
        .stream()
        .limit(count)
        .toArray(size -> (T[]) Array.newInstance(clazz, size));
  }

  /**
   * Generates an array of random instances of the specified class.
   *
   * @param clazz the class to generate
   * @param count the number of instances to generate
   * @return an array of random instances of the specified class
   */
  public static <T> T[] generate(Class<T> clazz, int count) {
    return Instancio.of(clazz)
        .stream()
        .limit(count)
        .toArray(size -> (T[]) Array.newInstance(clazz, size));
  }


}
