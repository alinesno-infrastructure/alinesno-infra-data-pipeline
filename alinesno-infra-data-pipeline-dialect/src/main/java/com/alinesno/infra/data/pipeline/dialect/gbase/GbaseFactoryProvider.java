// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.gbase;

import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.core.annotation.Product;
import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;
import com.alinesno.infra.data.pipeline.core.provider.AbstractFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.meta.MetadataProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.AutoCastTableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.TableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.AutoCastTableDataWriteProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.TableDataWriteProvider;
import com.alinesno.infra.data.pipeline.dialect.mysql.MysqlFeatures;

import javax.sql.DataSource;

@Product(ProductTypeEnum.GBASE8A)
public class GbaseFactoryProvider extends AbstractFactoryProvider {

  public GbaseFactoryProvider(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public ProductFeatures getProductFeatures() {
    return new MysqlFeatures();
  }

  @Override
  public MetadataProvider createMetadataQueryProvider() {
    return new GbaseMetadataQueryProvider(this);
  }

  @Override
  public TableDataWriteProvider createTableDataWriteProvider(boolean useInsert) {
    return new AutoCastTableDataWriteProvider(this);
  }

  @Override
  public TableDataSynchronizeProvider createTableDataSynchronizeProvider() {
    return new AutoCastTableDataSynchronizeProvider(this);
  }

}
