package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.converter;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.utils.TimeStampUtils;
import java.time.LocalDate;

public class LocalDateConverter implements IValueConverter<LocalDate, Integer> {

  @Override
  public Integer convert(final LocalDate date) {
    return TimeStampUtils.toPgDays(date);
  }

}
