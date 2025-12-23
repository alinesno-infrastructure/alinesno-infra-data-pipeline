package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.constants.DataType;

public interface IValueHandlerProvider {

  <TTargetType> IValueHandler<TTargetType> resolve(DataType targetType);
}
