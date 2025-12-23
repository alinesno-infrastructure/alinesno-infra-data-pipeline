// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: Li ZeMin (2413957313@qq.com)
// Date : 2024/12/16
// Location: nanjing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.tdengine;

import com.alinesno.infra.data.pipeline.core.provider.ProductFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.manage.DefaultTableManageProvider;

public class TdengineTableManageProvider extends DefaultTableManageProvider {

  public TdengineTableManageProvider(ProductFactoryProvider factoryProvider) {
    super(factoryProvider);
  }

  @Override
  public void truncateTableData(String schemaName, String tableName) {
    String sql = String.format("DELETE FROM %s.%s ",
        schemaName, tableName);
    this.executeSql(sql);
  }

  @Override
  public void dropTable(String schemaName, String tableName) {
    String sql = String.format("DROP TABLE %s.%s ",
        schemaName, tableName);
    this.executeSql(sql);
  }

}
