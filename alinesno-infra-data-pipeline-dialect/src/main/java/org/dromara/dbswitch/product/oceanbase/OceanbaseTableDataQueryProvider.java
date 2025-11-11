// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package org.dromara.dbswitch.product.oceanbase;

import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.core.provider.ProductFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.query.DefaultTableDataQueryProvider;
import com.alinesno.infra.data.pipeline.core.provider.query.TableDataQueryProvider;

public class OceanbaseTableDataQueryProvider extends DefaultTableDataQueryProvider implements TableDataQueryProvider {

  private final ProductTypeEnum dialect;

  public OceanbaseTableDataQueryProvider(ProductFactoryProvider factoryProvider,
      Boolean isMySqlMode) {
    super(factoryProvider);
    this.dialect = isMySqlMode ? ProductTypeEnum.MYSQL : ProductTypeEnum.ORACLE;
  }

  @Override
  protected String quoteName(String name) {
    return this.dialect.quoteName(name);
  }
}
