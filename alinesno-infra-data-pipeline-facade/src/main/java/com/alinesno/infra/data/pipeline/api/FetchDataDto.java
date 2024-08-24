package com.alinesno.infra.data.pipeline.api;

import lombok.Data;
import lombok.ToString;

/**
 * 获取数据源的DTO
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@ToString
@Data
public class FetchDataDto {
    private Long sourceId ; // 数据源ID
    private String sql; // SQL语句
}
