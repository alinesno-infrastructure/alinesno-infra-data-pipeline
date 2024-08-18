package com.alinesno.infra.data.pipeline.transfer;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.datasource.IDataTransferHandleService;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.enums.TransTypeEnums;
import com.alinesno.infra.data.pipeline.scheduler.IQuartzSchedulerService;
import com.alinesno.infra.data.pipeline.scheduler.dto.FilterPlugins;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.alinesno.infra.data.pipeline.service.IJobService;
import com.alinesno.infra.data.pipeline.service.ITransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据转移
 */
@Service
public class DataTransferHandleServiceImpl implements IDataTransferHandleService {

    private static final Logger log = LoggerFactory.getLogger(DataTransferHandleServiceImpl.class) ;

    @Autowired
    private IDataSourcePlugins dataSourcePlugins ;

    @Autowired
    private IJobService jobService ;

    @Autowired
    private ITransService transService ;

    @Autowired
    private IQuartzSchedulerService schedulerService ;

    @Override
    public void transferData(TaskInfoDto taskInfoDto) throws IOException, SQLException {

//        IDataSourceReader dataSourceReader = (IDataSourceReader) SpringContext.getBean(taskInfoDto.getReader().getType() + PipeConstants.READER_SUFFIX);
//        IDataSourceWriter dataSourceWriter = (IDataSourceWriter) SpringContext.getBean(taskInfoDto.getWriter().getType() + PipeConstants.WRITER_SUFFIX);

//        // 读取数据
//        String jobWorkspace = "" ;
//        File sourceFile = dataSourceReader.readData(taskInfoDto, jobWorkspace, trans) ;

//        // 数据插件处理
//        File filterFile = dataSourcePlugins.transformData(taskInfoDto , sourceFile) ;

//        // 写入数据
//        dataSourceWriter.writerData(taskInfoDto , filterFile, trans) ;

    }

    @Override
    public JobEntity analyzeTaskInfo(TaskInfoDto taskInfoDto) throws IOException, SQLException {

        // 创建一个Job
        JobEntity jobEntity = new JobEntity() ;

        jobEntity.setJobName(taskInfoDto.getName());
        jobEntity.setJobDesc(taskInfoDto.getDescribe());

        jobEntity.setSourceDbType(taskInfoDto.getReader().getType());
        jobEntity.setSourceDbId(taskInfoDto.getReader().getId());

        jobEntity.setTargetDbType(taskInfoDto.getWriter().getType());
        jobEntity.setTargetDbId(taskInfoDto.getWriter().getId());

        jobEntity.setJobCron(taskInfoDto.getSettings().getCron());
        jobEntity.setStartTime(taskInfoDto.getSettings().getStartTime());
        jobEntity.setEndTime(taskInfoDto.getSettings().getEndTime());

        jobEntity.setJobContext(JSONObject.toJSONString(taskInfoDto));
        jobService.save(jobEntity) ;

        // 创建任务节点
        List<TransEntity> listTrans = new ArrayList<>() ;

        // 获取多个Trans
        // 创建读取数据
        TransEntity readerTrans = new TransEntity() ;
        readerTrans.setJobId(jobEntity.getId());
        readerTrans.setOrderStep(1);

        readerTrans.setType(TransTypeEnums.READER.getCode());
        readerTrans.setName(taskInfoDto.getReader().getName());

        listTrans.add(readerTrans) ;

        // 处理插件
        genPluginsTrans(taskInfoDto.getPlugins() , listTrans , jobEntity)  ;

        // 创建写入数据
        TransEntity writerTrans= new TransEntity() ;
        writerTrans.setOrderStep(listTrans.size()+1);
        writerTrans.setJobId(jobEntity.getId());

        writerTrans.setType(TransTypeEnums.WRITER.getCode());
        writerTrans.setName(taskInfoDto.getWriter().getName());

        listTrans.add(writerTrans) ;
        transService.saveBatch(listTrans) ;

        return jobEntity ;
    }

    /**
     * 处理插件任务
     *
     * @param plugins
     * @param listTrans
     * @param jobEntity
     */
    private void genPluginsTrans(List<FilterPlugins> plugins, List<TransEntity> listTrans, JobEntity jobEntity) {

        if(!plugins.isEmpty()){

            for(FilterPlugins p : plugins){
                TransEntity t = new TransEntity() ;

                t.setJobId(jobEntity.getId());
                t.setOrderStep(listTrans.size()+1);
                t.setType(TransTypeEnums.PLUGIN.getCode());
                t.setName(p.getName());

                listTrans.add(t);
            }
        }

    }
}
