package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import lombok.NoArgsConstructor;

/**
 * 应用接入插件
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("plugins")
@NoArgsConstructor
public class ProviderChannelEntity extends InfraBaseEntity {

    /**
     * 插件名称
     */
    @ColumnType(length = 32)
    @ColumnComment("插件名称")
    @TableField
    private String channelName;

    /**
     * 插件描述
     */
    @ColumnType(length = 256)
    @ColumnComment("插件名称")
    @TableField
    private String channelDesc;

    /**
     * 图标
     */
    @ColumnType(length = 32)
    @ColumnComment("图标")
    @TableField
    private String icon;

    /**
     * 是否打开
     */
    @ColumnType(length = 1)
    @ColumnComment("是否打开")
    @TableField
    private Integer isOpen;

    /**
     * 请求次数
     */
    @ColumnType(length = 11)
    @ColumnComment("请求次数")
    @TableField
    private Integer requestCount;

    /**
     * 是否限流
     */
    @ColumnType(length = 1)
    @ColumnComment("是否限流")
    @TableField
    private Integer isRateLimited;

    public ProviderChannelEntity(String icon, String channelName, String channelDesc, boolean isOpen, int requestCount, boolean isRateLimited) {
        this.icon = icon;
        this.channelName = channelName;
        this.channelDesc = channelDesc;
        this.isOpen = isOpen?1:0;
        this.requestCount = requestCount;
        this.isRateLimited = isRateLimited?1:0;
    }

}
