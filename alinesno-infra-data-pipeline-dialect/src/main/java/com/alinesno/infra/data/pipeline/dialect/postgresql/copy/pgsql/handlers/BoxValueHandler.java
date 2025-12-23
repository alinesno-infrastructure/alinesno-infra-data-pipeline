package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers.utils.GeometricUtils;
import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.model.geometric.Box;
import java.io.DataOutputStream;
import java.io.IOException;

public class BoxValueHandler extends BaseValueHandler<Box> {

  @Override
  protected void internalHandle(DataOutputStream buffer, final Box value) throws IOException {
    buffer.writeInt(32);

    GeometricUtils.writePoint(buffer, value.getHigh());
    GeometricUtils.writePoint(buffer, value.getLow());
  }

  @Override
  public int getLength(Box value) {
    return 32;
  }
}