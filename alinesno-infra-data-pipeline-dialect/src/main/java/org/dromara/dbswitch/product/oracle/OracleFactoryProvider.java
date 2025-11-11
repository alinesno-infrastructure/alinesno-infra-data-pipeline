// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package org.dromara.dbswitch.product.oracle;

import com.alinesno.infra.data.pipeline.core.annotation.Product;
import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.core.features.DefaultProductFeatures;
import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;
import com.alinesno.infra.data.pipeline.core.provider.AbstractFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.meta.MetadataProvider;
import com.alinesno.infra.data.pipeline.core.provider.manage.TableManageProvider;
import com.alinesno.infra.data.pipeline.core.provider.query.TableDataQueryProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.TableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.TableDataWriteProvider;
import javax.sql.DataSource;

@Product(ProductTypeEnum.ORACLE)
public class OracleFactoryProvider extends AbstractFactoryProvider {

  public OracleFactoryProvider(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public ProductFeatures getProductFeatures() {
    return new DefaultProductFeatures();
  }

  @Override
  public MetadataProvider createMetadataQueryProvider() {
    return new OracleMetadataQueryProvider(this);
  }

  @Override
  public TableDataQueryProvider createTableDataQueryProvider() {
    return new OracleTableDataQueryProvider(this);
  }

  @Override
  public TableDataWriteProvider createTableDataWriteProvider(boolean useInsert) {
    return new OracleTableDataWriteProvider(this);
  }

  @Override
  public TableManageProvider createTableManageProvider() {
    return new OracleTableManageProvider(this);
  }

  @Override
  public TableDataSynchronizeProvider createTableDataSynchronizeProvider() {
    return new OracleTableDataSynchronizer(this);
  }

}
