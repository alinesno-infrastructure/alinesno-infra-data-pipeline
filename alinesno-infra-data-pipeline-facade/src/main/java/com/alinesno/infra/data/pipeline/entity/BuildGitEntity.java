package com.alinesno.infra.data.pipeline.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import lombok.Data;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * gitLab仓库地址信息
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("build_git")
@Data
public class BuildGitEntity extends InfraBaseEntity {

    /**
     * 仓库名称
     */
    @TableField("git_name")
	@ColumnType(length=255)
	@ColumnComment("仓库名称")
    private String gitName;

    /**
     * 仓库图标
     */
    @TableField("git_icon")
	@ColumnType(length=50)
	@ColumnComment("仓库图标")
    private String gitIcon;

    /**
     * 仓库地址
     */
    @TableField("git_host")
	@ColumnType(length=255)
	@ColumnComment("仓库地址")
    private String gitHost;

    /**
     * 仓库地址
     */
    @TableField("git_url")
	@ColumnType(length=255)
	@ColumnComment("仓库地址")
    private String gitUrl; // 如 http://192.168.204.130:19802/gitlab-instance-ddd728ad/alinesno-cloud-data-etl-databrain.git

    /**
     * 项目分支
     */
    @TableField("branch_name")
	@ColumnType(length=50)
	@ColumnComment("项目分支")
    private String branchName;

    /**
     * gitlab账号
     */
    @TableField("git_user_name")
	@ColumnType(length=255)
	@ColumnComment("gitlab账号")
    private String gitUserName; // 仓库账号

    /**
     * gitlab账号密码
     */
    @TableField("password")
	@ColumnType(length=64)
	@ColumnComment("gitlab账号密码")
    private String password;

    /**
     * gitlab账号id
     */
    @TableField("git_user_id")
	@ColumnType(length=20)
	@ColumnComment("gitlab账号id")
    private int gitUserId; // 仓库账号

    /**
     * 仓库空间
     */
    @TableField
	@ColumnType(length=255)
	@ColumnComment("仓库空间")
    private String gitNamespace; // 仓库空间

    /**
     * 仓库类型
     */
    @TableField
	@ColumnType(length=20)
	@ColumnComment("仓库类型")
    private String gitType; // 仓库类型

    /**
     * 内置gitlab
     */
    @TableField("inner_git")
	@ColumnType(length=255)
	@ColumnComment("内置gitlab")
    private int innerGit;

    /**
     * 是否绑定
     */
    @TableField("has_bing")
	@ColumnType(length=255)
	@ColumnComment("是否绑定")
    private int hasBing; // 是否绑定

    /**
     * 刷新token
     */
    @TableField("refresh_token")
	@ColumnType(length=255)
	@ColumnComment("刷新token")
    private String refreshToken;

    /**
     * 超时时间
     */
    @TableField("expires_in")
	@ColumnType(length=255)
	@ColumnComment("超时时间")
    private int expiresIn;

    /**
     * 绑定第三方git账号信息
     */
    @TableField("bing_git_info")
	@ColumnType(length=255)
	@ColumnComment("绑定第三方git账号信息")
    private String bingGitInfo; // 绑定第三方git账号信息
}
