package com.alinesno.infra.data.pipeline.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.enums.PluginEnum;
import com.alinesno.infra.data.pipeline.scheduler.enums.SinkReaderEnums;
import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;
import com.alinesno.infra.data.pipeline.service.IJobService;
import com.alinesno.infra.data.pipeline.service.IReaderSourceService;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理任务构建的请求
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "Job")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/pipeline/jobBuilder")
public class JobBuilderController extends BaseController<JobEntity, IJobService> {

    @Autowired
    private IReaderSourceService readerSourceService ;

    // 获取到所有的Reader列表和Sink列表
    @GetMapping("/getAllSourceReader")
    public AjaxResult getAllSourceReader() {

        List<ReaderSourceEntity> values = readerSourceService.list() ;
        return AjaxResult.success(values);
    }

    // 获取到所有插件的列表
    @GetMapping("/getAllPlugin")
    public AjaxResult getAllPlugin() {

        PluginEnum[] names = PluginEnum.values();
        List<PluginBean> list = new ArrayList<>();

        for(PluginEnum name : names){
            PluginBean pluginBean = new PluginBean(name.getName(), name.getDesc(), name.getIcon());
            list.add(pluginBean);
        }

        return AjaxResult.success(list);
    }

    @Data
    static class PluginBean{

        private final String name;
        private final String desc;
        private final String icon;

        public PluginBean(String name, String desc, String icon) {
            this.name = name;
            this.desc = desc;
            this.icon = icon;
        }
    }

}
