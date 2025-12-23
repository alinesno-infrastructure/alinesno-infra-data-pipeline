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
import com.alinesno.infra.data.pipeline.core.provider.sync.AutoCastTableDataSynchronizeProvider;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TdengineTableSynchronizer extends AutoCastTableDataSynchronizeProvider {

  public TdengineTableSynchronizer(ProductFactoryProvider factoryProvider) {
    super(factoryProvider);
  }

  @Override
  protected TransactionDefinition getDefaultTransactionDefinition() {
    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    return definition;
  }

}
