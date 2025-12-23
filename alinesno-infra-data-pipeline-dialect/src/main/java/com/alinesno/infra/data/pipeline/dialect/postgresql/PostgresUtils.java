// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.postgresql;

import com.alinesno.infra.data.pipeline.core.provider.meta.MetadataProvider;
import com.alinesno.infra.data.pipeline.core.schema.ColumnDescription;
import com.alinesno.infra.data.pipeline.core.util.GenerateSqlUtils;
import java.sql.Connection;
import java.util.List;

public final class PostgresUtils {

  public static String getTableDDL(MetadataProvider provider, Connection connection, String schema,
      String table) {
    List<ColumnDescription> columnDescriptions = provider.queryTableColumnMeta(connection, schema, table);
    List<String> pks = provider.queryTablePrimaryKeys(connection, schema, table);
    return GenerateSqlUtils.getDDLCreateTableSQL(
        provider, columnDescriptions, pks, schema, table, false);
  }


  private PostgresUtils() {
    throw new IllegalStateException();
  }

}
