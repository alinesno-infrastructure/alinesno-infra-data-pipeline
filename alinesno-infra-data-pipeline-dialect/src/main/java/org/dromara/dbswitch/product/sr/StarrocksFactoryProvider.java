// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package org.dromara.dbswitch.product.sr;

import javax.sql.DataSource;
import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.core.annotation.Product;
import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;
import com.alinesno.infra.data.pipeline.core.provider.AbstractFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.meta.MetadataProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.TableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.TableDataWriteProvider;

@Product(ProductTypeEnum.STARROCKS)
public class StarrocksFactoryProvider extends AbstractFactoryProvider {

  public StarrocksFactoryProvider(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public ProductFeatures getProductFeatures() {
    return new StarrocksFeatures();
  }

  @Override
  public MetadataProvider createMetadataQueryProvider() {
    return new StarrocksMetadataQueryProvider(this);
  }

  @Override
  public TableDataWriteProvider createTableDataWriteProvider(boolean useInsert) {
    return new StarrocksTableDataWriteProvider(this);
  }

  @Override
  public TableDataSynchronizeProvider createTableDataSynchronizeProvider() {
    return new StarrocksTableDataSynchronizer(this);
  }

}
