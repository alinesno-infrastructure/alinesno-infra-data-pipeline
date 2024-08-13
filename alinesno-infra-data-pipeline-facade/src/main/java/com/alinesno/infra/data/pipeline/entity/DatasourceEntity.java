package com.alinesno.infra.data.pipeline.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.alinesno.infra.common.security.mapper.AESEncryptHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "pipeline_datasource" , autoResultMap = true)
@Data
public class DatasourceEntity extends InfraBaseEntity {

    @ColumnComment("描述")
    @Excel(name = "描述")
    @TableField("db_desc")
    private String dbDesc;

    // fields
    /**
     * 数据库名称
     */
    @ColumnComment("数据库名称")
    @Excel(name = "数据库名称")
    @TableField("db_name")
    private String dbName;
    /**
     * 数据库连接
     */
    @ColumnComment("数据库连接")
    @Excel(name = "数据库连接")
    @TableField(value = "db_url" , typeHandler = AESEncryptHandler.class)
    private String dbUrl;

    /**
     * 数据库连接
     */
    @ColumnComment("数据库连接地址")
    @Excel(name = "数据库连接地址")
    @TableField(value = "jdbc_url" , typeHandler = AESEncryptHandler.class)
    private String jdbcUrl;

    /**
     * 数据库用户名
     */
    @ColumnComment("数据库用户名")
    @Excel(name = "数据库用户名")
    @TableField(value = "db_username" , typeHandler = AESEncryptHandler.class)
    private String dbUsername;
    /**
     * 数据库密码
     */
    @ColumnComment("数据库密码")
    @Excel(name = "数据库密码")
    @TableField(value = "db_passwd" , typeHandler = AESEncryptHandler.class)
    private String dbPasswd;
    /**
     * 数据库连接端口
     */
    @ColumnComment("数据库连接端口")
    @Excel(name = "数据库连接端口")
    @TableField("db_port")
    private String dbPort;

    /**
     * 数据库类型
     */
    @ColumnComment("数据库类型")
    @Excel(name = "数据库类型")
    @TableField("db_type")
    private String dbType;

    @ColumnComment("作者名")
    @Excel(name = "作者名")
    @TableField("author")
    private String author;

}
