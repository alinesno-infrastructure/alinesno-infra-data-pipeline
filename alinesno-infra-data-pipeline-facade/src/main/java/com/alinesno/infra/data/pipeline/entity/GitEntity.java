package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * gitLab仓库地址信息
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pipeline_git")
@Table(comment = "gitLab仓库地址信息")
public class GitEntity extends InfraBaseEntity {

    /**
     * 仓库名称
     */
    @TableField("git_name")
    private String gitName;

    /**
     * 仓库图标
     */
    @TableField("git_icon")
    private String gitIcon;

    /**
     * 仓库地址
     */
    @TableField("git_host")
    private String gitHost;

    /**
     * 仓库地址
     */
    @TableField("git_url")
    private String gitUrl; // 如 http://192.168.204.130:19802/gitlab-instance-ddd728ad/alinesno-cloud-data-etl-databrain.git

    /**
     * 项目分支
     */
    @TableField("branch_name")
    private String branchName;

    /**
     * gitlab账号
     */
    @TableField("git_user_name")
    private String gitUserName; // 仓库账号

    /**
     * gitlab账号密码
     */
    @TableField("password")
    private String password;

    /**
     * gitlab账号id
     */
    @TableField("git_user_id")
    private int gitUserId; // 仓库账号

    /**
     * 仓库空间
     */
    @TableField
    private String gitNamespace; // 仓库空间

    /**
     * 仓库类型
     */
    @TableField
    private String gitType; // 仓库类型

    /**
     * 内置gitlab
     */
    @TableField("inner_git")
    private int innerGit;

    /**
     * 是否绑定
     */
    @TableField("has_bing")
    private int hasBing; // 是否绑定

    /**
     * 刷新token
     */
    @TableField("refresh_token")
    private String refreshToken;

    /**
     * 超时时间
     */
    @TableField("expires_in")
    private int expiresIn;

    /**
     * 绑定第三方git账号信息
     */
    @TableField("bing_git_info")
    private String bingGitInfo; // 绑定第三方git账号信息

}
