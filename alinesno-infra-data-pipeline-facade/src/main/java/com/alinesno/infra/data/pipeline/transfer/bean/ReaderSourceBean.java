package com.alinesno.infra.data.pipeline.transfer.bean;

import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import lombok.Data;

/**
 * 读取源信息
 * @author luoxiaodong
 * @version 1.0.0
 */
@Data
public class ReaderSourceBean {

    private ReaderSourceEntity source; // 读取源信息
    private String sql; // 操作的SQL语句

}
