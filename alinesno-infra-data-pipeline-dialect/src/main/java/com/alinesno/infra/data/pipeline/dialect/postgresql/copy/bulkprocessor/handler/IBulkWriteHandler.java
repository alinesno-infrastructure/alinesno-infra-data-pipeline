package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.bulkprocessor.handler;

import java.util.List;

public interface IBulkWriteHandler<TEntity> {

  void write(List<TEntity> entities) throws Exception;

}
