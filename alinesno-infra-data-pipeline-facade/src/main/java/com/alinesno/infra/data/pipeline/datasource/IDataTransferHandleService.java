package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 数据源处理
 */
public interface IDataTransferHandleService {

    /**
     * 数据转移
     * @param taskInfoDto
     */
    public void transferData(TaskInfoDto taskInfoDto) throws IOException, SQLException;

    /**
     * 分析任务信息
     *
     * @param taskInfoDto
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public JobEntity analyzeTaskInfo(TaskInfoDto taskInfoDto) throws IOException, SQLException;

}
