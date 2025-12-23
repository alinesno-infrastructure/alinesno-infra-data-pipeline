package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.converter;

public interface IValueConverter<TSource, TTarget> {

  TTarget convert(TSource source);

}
