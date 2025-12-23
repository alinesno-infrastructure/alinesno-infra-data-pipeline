package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.function;

@FunctionalInterface
public interface ToFloatFunction<T> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  float applyAsFloat(T value);
}
