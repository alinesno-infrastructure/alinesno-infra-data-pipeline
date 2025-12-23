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

import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.core.annotation.Product;
import com.alinesno.infra.data.pipeline.core.features.DefaultProductFeatures;
import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;
import com.alinesno.infra.data.pipeline.core.provider.AbstractFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.manage.TableManageProvider;
import com.alinesno.infra.data.pipeline.core.provider.meta.MetadataProvider;
import com.alinesno.infra.data.pipeline.core.provider.query.TableDataQueryProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.TableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.TableDataWriteProvider;

import javax.sql.DataSource;

@Product(ProductTypeEnum.TDENGINE)
public class TdengineFactoryProvider extends AbstractFactoryProvider {

  public TdengineFactoryProvider(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public ProductFeatures getProductFeatures() {
    return new DefaultProductFeatures();
  }

  @Override
  public MetadataProvider createMetadataQueryProvider() {
    return new TdengineMetadataQueryProvider(this);
  }

  @Override
  public TableDataWriteProvider createTableDataWriteProvider(boolean useInsert) {
    return new TDengineTableDataWriteProvider(this);
  }

  @Override
  public TableDataSynchronizeProvider createTableDataSynchronizeProvider() {
    return new TdengineTableSynchronizer(this);
  }

  @Override
  public TableDataQueryProvider createTableDataQueryProvider() {
    return new TdengineTableDataQueryProvider(this);
  }

  @Override
  public TableManageProvider createTableManageProvider() {
    return new TdengineTableManageProvider(this);
  }

}
