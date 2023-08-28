package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 功能名： 【请填写功能名称】
 * 数据表：  category
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("category")
public class CategoryEntity extends InfraBaseEntity {

    /**
     * categoryName
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 获取 categoryName
     */
    public String getCategoryName() {
        return this.categoryName;
    }

    /**
     * 设置 categoryName
     */
    public CategoryEntity setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }
}
