package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.converter;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.utils.TimeStampUtils;
import java.time.LocalDateTime;

public class LocalDateTimeConverter implements IValueConverter<LocalDateTime, Long> {

  @Override
  public Long convert(final LocalDateTime dateTime) {
    return TimeStampUtils.convertToPostgresTimeStamp(dateTime);
  }
}
