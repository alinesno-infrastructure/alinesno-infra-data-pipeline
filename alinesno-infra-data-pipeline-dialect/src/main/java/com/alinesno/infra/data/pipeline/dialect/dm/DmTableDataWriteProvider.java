// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.dm;

import com.alinesno.infra.data.pipeline.common.util.ObjectCastUtils;
import com.alinesno.infra.data.pipeline.core.provider.ProductFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.DefaultTableDataWriteProvider;

import java.util.List;

public class DmTableDataWriteProvider extends DefaultTableDataWriteProvider {

  public DmTableDataWriteProvider(ProductFactoryProvider factoryProvider) {
    super(factoryProvider);
  }

  @Override
  public long write(List<String> fieldNames, List<Object[]> recordValues) {
    recordValues.parallelStream().forEach((Object[] row) -> {
      for (int i = 0; i < row.length; ++i) {
        row[i] = ObjectCastUtils.castByDetermine(row[i]);
      }
    });
    return super.write(fieldNames, recordValues);
  }
}
