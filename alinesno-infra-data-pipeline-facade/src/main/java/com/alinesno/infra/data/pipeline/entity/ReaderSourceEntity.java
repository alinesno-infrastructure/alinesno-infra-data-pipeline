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
@TableName(value = "pipeline_reader_source" , autoResultMap = true)
@Data
public class ReaderSourceEntity extends InfraBaseEntity {

    @ColumnComment("描述")
    @Excel(name = "描述")
    @TableField("reader_desc")
    private String readerDesc;

    // fields
    /**
     * 读取源名称
     */
    @ColumnComment("读取源名称")
    @Excel(name = "读取源名称")
    @TableField("reader_name")
    private String readerName;
    /**
     * 读取源连接
     */
    @ColumnComment("读取源连接")
    @Excel(name = "读取源连接")
    @TableField(value = "reader_url" , typeHandler = AESEncryptHandler.class)
    private String readerUrl;

    /**
     * 读取源用户名
     */
    @ColumnComment("读取源用户名")
    @Excel(name = "读取源用户名")
    @TableField(value = "reader_username" , typeHandler = AESEncryptHandler.class)
    private String readerUsername;
    /**
     * 读取源密码
     */
    @ColumnComment("读取源密码")
    @Excel(name = "读取源密码")
    @TableField(value = "reader_passwd" , typeHandler = AESEncryptHandler.class)
    private String readerPasswd;
    /**
     * 读取源连接端口
     */
    @ColumnComment("读取源连接端口")
    @Excel(name = "读取源连接端口")
    @TableField("reader_port")
    private String readerPort;

    /**
     * 读取源类型
     */
    @ColumnComment("读取源类型")
    @Excel(name = "读取源类型")
    @TableField("reader_type")
    private String readerType;

    @ColumnComment("作者名")
    @Excel(name = "作者名")
    @TableField("author")
    private String author;

    // 类型（读取/写入)
    @ColumnComment("类型（读取/写入)")
    @Excel(name = "类型（读取/写入)")
    @TableField("operation_type")
    private String operationType;

}
