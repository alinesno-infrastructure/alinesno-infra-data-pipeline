package com.alinesno.infra.data.pipeline.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.pipeline.entity.PluginsEntity;
import com.alinesno.infra.data.pipeline.service.IPluginsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 处理与ProviderChannelEntity相关的请求的Controller。
 * 继承自BaseController类并实现IProviderChannelService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "ProviderChannel")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/pipeline/plugins")
public class PluginsController extends BaseController<PluginsEntity, IPluginsService> {

    @Autowired
    private IPluginsService service;

    /**
     * 获取ProviderChannelEntity的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model   Model对象。
     * @param page    DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));

        long userId = 1L ; // CurrentAccountJwt.getUserId();
        long countGitRepository = service.count(new LambdaQueryWrapper<PluginsEntity>().eq(PluginsEntity::getOperatorId , userId));

        // 初始化用户仓库
        if (countGitRepository == 0) {
            service.initProviderChannel(userId) ;
        }

        return this.toPage(model, this.getFeign(), page);
    }

    /**
     * 列出所有渠道类型
     * @return
     */
    @GetMapping("/listAllChannel")
    public AjaxResult listAllChannel(){
        return AjaxResult.success(service.list()) ;
    }

    @Override
    public IPluginsService getFeign() {
        return this.service;
    }
}