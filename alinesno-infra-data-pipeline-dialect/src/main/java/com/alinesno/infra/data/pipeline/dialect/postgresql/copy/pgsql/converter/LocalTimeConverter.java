package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.converter;

import java.time.LocalTime;

public class LocalTimeConverter implements IValueConverter<LocalTime, Long> {

  @Override
  public Long convert(final LocalTime time) {
    return time.toNanoOfDay() / 1000L;
  }

}
