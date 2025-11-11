// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.transfer.domain;

import cn.hutool.core.io.unit.DataSizeUtil;
import com.alinesno.infra.data.pipeline.common.entity.PrintablePerfStat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 并发读取统计格式化信息
 *
 * @author tang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderPerfStat extends PrintablePerfStat {

  private long total;
  private long failure;
  private long bytes;

  @Override
  public String getPrintableString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Total Read Tables Count: \t" + total + "\n");
    sb.append("Total Failure Tables Count: \t" + failure + "\n");
    sb.append("Total Read Record Size: \t" + DataSizeUtil.format(bytes) + "\n");
    return sb.toString();
  }
}
