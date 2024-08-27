package com.alinesno.infra.data.pipeline.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.pipeline.api.dto.JobInstanceDto;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.JobInstanceEntity;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import com.alinesno.infra.data.pipeline.service.IJobService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理与JobEntity相关的请求的Controller。
 * 继承自BaseController类并实现IJobService接口。
 *
 * @version 1.0.0
 * @author  luoxiaodong
 */
@Slf4j
@Api(tags = "Job")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/pipeline/jobInstance")
public class JobInstanceController extends BaseController<JobInstanceEntity, IJobInstanceService> {

    @Autowired
    private IJobInstanceService service;

    @Autowired
    private IJobService jobService ;

    /**
     * 获取JobEntity的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param page    DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        TableDataInfo tableDataInfo = this.toPage(model, this.getFeign(), page);

        Map<Long , JobEntity> jobEntities = new HashMap<>() ;
        List<JobInstanceDto> list = new ArrayList<>() ;
        List<Long> ids = new ArrayList<> () ;

        tableDataInfo.getRows().forEach(item -> {
            ids.add(((JobInstanceEntity)item).getJobId()) ;
        }) ;

        if (!ids.isEmpty()) {
            List<JobEntity> jobE = jobService.listByIds(ids) ;
            jobEntities = jobE.stream().collect(HashMap::new , (m , v) -> m.put(v.getId() , v) , HashMap::putAll) ;
        }

        Map<Long, JobEntity> finalJobEntities = jobEntities;
        tableDataInfo.getRows().forEach(item -> {
            JobInstanceEntity e = (JobInstanceEntity) item ;
            JobInstanceDto dto = new JobInstanceDto() ;
            BeanUtils.copyProperties(e , dto);

            JobEntity job = finalJobEntities.get(e.getJobId()) ;

            dto.setStatusLabel(JobStatusEnums.getLabelByStatus(dto.getStatus()));
            dto.setJobName(job.getJobName()) ;
            dto.setJobDesc(job.getJobDesc());
            dto.setTargetDbType(job.getTargetDbType());
            dto.setSourceDbType(job.getSourceDbType());

            dto.setReaderCount(service.getReaderCount(e.getId()));
            dto.setWriterCount(service.getWriterCount(e.getId()));

            list.add(dto) ;
        }) ;

        tableDataInfo.setRows(list);

        return tableDataInfo;
    }

    @Override
    public IJobInstanceService getFeign() {
        return this.service;
    }
}
