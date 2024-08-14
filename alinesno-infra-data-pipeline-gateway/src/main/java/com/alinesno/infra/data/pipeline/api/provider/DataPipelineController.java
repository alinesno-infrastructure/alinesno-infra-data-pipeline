package com.alinesno.infra.data.pipeline.api.provider;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.data.pipeline.datasource.IDataTransferHandleService;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 构建任务对外服务
 */
@Slf4j
@Scope
@RestController
@RequestMapping("/v1/api/data/pipeline/")
public class DataPipelineController {

    @Autowired
    private IDataTransferHandleService dataTransferHandleService ;

    /**
     * 运行数据抽取服务
     * @param taskInfoDto
     * @return
     */
    @PostMapping("runPipeline")
    public AjaxResult runPipeline(@RequestBody TaskInfoDto taskInfoDto) throws SQLException, IOException {

        log.debug("taskInfo = {}" , JSONObject.toJSON(taskInfoDto));

        JobEntity job = dataTransferHandleService.analyzeTaskInfo(taskInfoDto);

        return AjaxResult.success("任务保存成功." , job.getId());
    }

}
