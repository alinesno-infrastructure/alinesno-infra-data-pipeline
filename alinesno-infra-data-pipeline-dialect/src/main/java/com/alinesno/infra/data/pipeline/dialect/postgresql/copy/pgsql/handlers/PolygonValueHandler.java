package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers.utils.GeometricUtils;
import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.model.geometric.Point;
import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.model.geometric.Polygon;
import java.io.DataOutputStream;
import java.io.IOException;

public class PolygonValueHandler extends BaseValueHandler<Polygon> {

  @Override
  protected void internalHandle(DataOutputStream buffer, final Polygon value) throws IOException {
    // The total number of bytes to write:
    int totalBytesToWrite = 4 + 16 * value.size();

    // The Number of Bytes to follow:
    buffer.writeInt(totalBytesToWrite);

    // Write Points:
    buffer.writeInt(value.getPoints().size());

    // Write each Point in List:
    for (Point p : value.getPoints()) {
      GeometricUtils.writePoint(buffer, p);
    }

  }

  @Override
  public int getLength(Polygon value) {
    throw new UnsupportedOperationException();
  }
}