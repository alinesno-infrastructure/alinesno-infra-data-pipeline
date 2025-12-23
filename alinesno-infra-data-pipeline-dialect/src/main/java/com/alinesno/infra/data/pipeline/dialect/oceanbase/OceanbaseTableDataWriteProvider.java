// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.oceanbase;

import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.dialect.oracle.OracleTableDataWriteProvider;
import com.alinesno.infra.data.pipeline.core.provider.ProductFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.DefaultTableDataWriteProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.TableDataWriteProvider;
import java.util.List;

public class OceanbaseTableDataWriteProvider extends DefaultTableDataWriteProvider {

  private final TableDataWriteProvider delegate;
  private final ProductTypeEnum dialect;

  public OceanbaseTableDataWriteProvider(ProductFactoryProvider factoryProvider, TableDataWriteProvider delegate) {
    super(factoryProvider);
    this.delegate = delegate;
    if (delegate instanceof OracleTableDataWriteProvider) {
      this.dialect = ProductTypeEnum.ORACLE;
    } else {
      this.dialect = ProductTypeEnum.MYSQL;
    }
  }

  @Override
  protected String quoteName(String name) {
    return this.dialect.quoteName(name);
  }

  @Override
  protected String quoteSchemaTableName(String schemaName, String tableName) {
    return this.dialect.quoteSchemaTableName(schemaName, tableName);
  }

  @Override
  public void prepareWrite(String schemaName, String tableName, List<String> fieldNames) {
    this.delegate.prepareWrite(schemaName, tableName, fieldNames);
  }

  @Override
  public long write(List<String> fieldNames, List<Object[]> recordValues) {
    return this.delegate.write(fieldNames, recordValues);
  }
}
