package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers.utils.GeometricUtils;
import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.model.geometric.Point;
import java.io.DataOutputStream;
import java.io.IOException;

public class PointValueHandler extends BaseValueHandler<Point> {

  @Override
  protected void internalHandle(DataOutputStream buffer, final Point value) throws IOException {
    buffer.writeInt(16);

    GeometricUtils.writePoint(buffer, value);
  }

  @Override
  public int getLength(Point value) {
    return 16;
  }
}