package com.alinesno.infra.common.web.adapter.login.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.dto.LoginBodyDto;
import com.alinesno.infra.common.web.adapter.dto.menus.Menu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class CommonLoginController {

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBodyDto loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = UUID.randomUUID().toString() ;
        ajax.put(TOKEN, token);
        return ajax;
    }

    /**
     * 退出登陆
     * @return
     */
    @PostMapping("/logout")
    public AjaxResult logout() {
        StpUtil.logout();
        return AjaxResult.success();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {

        Map<String, Object> data = new HashMap<>();
        // 将数据填充到data中...
        data.put("permissions", new String[]{"*:*:*"});

        Map<String, Object> user = new HashMap<>();
        user.put("createBy", "admin");
        user.put("createTime", "2023-04-23 16:11:38");
        user.put("updateBy", null);
        user.put("updateTime", null);
        user.put("remark", "管理员");
        user.put("userId", 1);
        user.put("deptId", 103);
        user.put("userName", "admin");
        user.put("nickName", "AIP技术团队");
        user.put("email", "aip-team@163.com");
        user.put("phonenumber", "15888888888");
        user.put("sex", "1");
        user.put("avatar", "");
        user.put("password", "");
        user.put("status", "0");
        user.put("delFlag", "0");
        user.put("loginIp", "");
        user.put("loginDate", "2023-09-21T16:54:12.000+08:00");

        Map<String, Object> dept = new HashMap<>();
        dept.put("createBy", null);
        dept.put("createTime", null);
        dept.put("updateBy", null);
        dept.put("updateTime", null);
        dept.put("remark", null);
        dept.put("deptId", 103);
        dept.put("parentId", 101);
        dept.put("ancestors", "0,100,101");
        dept.put("deptName", "研发部门");
        dept.put("orderNum", 1);
        dept.put("leader", "AIP技术团队");
        dept.put("phone", null);
        dept.put("email", null);
        dept.put("status", "0");
        dept.put("delFlag", null);
        dept.put("parentName", null);
        dept.put("children", new Object[]{});

        user.put("dept", dept);

        Map<String, Object> role = new HashMap<>();
        role.put("createBy", null);
        role.put("createTime", null);
        role.put("updateBy", null);
        role.put("updateTime", null);
        role.put("remark", null);
        role.put("roleId", 1);
        role.put("roleName", "超级管理员");
        role.put("roleKey", "admin");
        role.put("roleSort", 1);
        role.put("dataScope", "1");
        role.put("menuCheckStrictly", false);
        role.put("deptCheckStrictly", false);
        role.put("status", "0");
        role.put("delFlag", null);
        role.put("flag", false);
        role.put("menuIds", null);
        role.put("deptIds", null);
        role.put("permissions", null);
        role.put("admin", true);

        user.put("roles", new Object[]{role});

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", user.get("roles"));
        ajax.put("permissions", data.get("permissions"));

        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {

        Menu dashboardMenu = new Menu("概览", "/dashboard", false, "noRedirect", "Layout", true,
                new Menu.Meta("概览", "dashboard", false, null),
                List.of(
                        new Menu("仪盘表", "index", false, false, "dashboard", new Menu.Meta("仪盘表", "dashboard", false, null)),
                        new Menu("项目管理", "data/pipeline/project/index", false, false, "data/pipeline/project/index", new Menu.Meta("项目管理", "monitor", false, null))
                ));

        Menu taskManagementMenu = new Menu("任务管理", "/task", false, "noRedirect", "Layout", true,
                new Menu.Meta("任务管理", "post", false, null),
                List.of(
                        new Menu("创建任务", "data/pipeline/task/create", false, false, "data/pipeline/task/create", new Menu.Meta("任务模板", "peoples", false, null)),
                        new Menu("任务执行中", "data/pipeline/task/status", false, false, "data/pipeline/task/status", new Menu.Meta("任务执行中", "tree", false, null)),
                        new Menu("异常历史", "data/pipeline/task/history", false, false, "data/pipeline/task/history", new Menu.Meta("异常任务", "message", false, null))
                ));

        Menu migrationManagementMenu = new Menu("迁移管理", "/migration", false, "noRedirect", "Layout", true,
                new Menu.Meta("迁移管理", "monitor", false, null),
                List.of(
                        new Menu("插件管理", "data/pipeline/plugins/index", false, false, "data/pipeline/plugins/index", new Menu.Meta("插件管理", "peoples", false, null)),
                        new Menu("数据接入", "data/pipeline/datasource/index", false, false, "data/pipeline/datasource/index", new Menu.Meta("数据源管理", "server", false, null))
                ));

        Menu operationMonitoringMenu = new Menu("运行监控", "/operation-monitoring", false, "noRedirect", "Layout", true,
                new Menu.Meta("运行监控", "monitor", false, null),
                List.of(
                        new Menu("迁移日志", "data/pipeline/record/index", false, false, "data/pipeline/record/index", new Menu.Meta("迁移日志", "redis", false, null)),
                        new Menu("运行监控", "data/pipeline/monitor/index", false, false, "data/pipeline/monitor/index", new Menu.Meta("运行监控", "redis", false, null))
                ));

        List<Menu> menus = List.of(dashboardMenu, taskManagementMenu, migrationManagementMenu, operationMonitoringMenu);
        return AjaxResult.success(menus);
    }
}
