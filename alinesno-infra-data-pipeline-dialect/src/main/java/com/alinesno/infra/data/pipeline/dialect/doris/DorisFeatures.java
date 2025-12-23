// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: wjk (wanglv110@163.com)
// Date : 2024/9/29
// Location: wuhan , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.doris;

import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;

public class DorisFeatures implements ProductFeatures {

  @Override
  public int convertFetchSize(int fetchSize) {
    return Integer.MIN_VALUE;
  }

}
