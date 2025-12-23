package com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.handlers;

import com.alinesno.infra.data.pipeline.dialect.postgresql.copy.pgsql.model.network.MacAddress;
import java.io.DataOutputStream;
import java.io.IOException;

public class MacAddressValueHandler extends BaseValueHandler<MacAddress> {

  @Override
  protected void internalHandle(DataOutputStream buffer, final MacAddress value)
      throws IOException {
    buffer.writeInt(6);
    buffer.write(value.getAddressBytes());
  }

  @Override
  public int getLength(MacAddress value) {
    return 6;
  }
}
