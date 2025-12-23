// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.startrocks;

import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;

public class StarrocksFeatures implements ProductFeatures {

  @Override
  public int convertFetchSize(int fetchSize) {
    return Integer.MIN_VALUE;
  }

}
