package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.alinesno.infra.data.pipeline.transfer.bean.FieldMetaBean;
import com.alinesno.infra.data.pipeline.transfer.bean.ReaderSourceBean;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据读取
 */
public interface IDataSourceReader {

    /**
     * 读取数据
     * @param taskInfoDto
     * @param jobWorkspace
     * @param trans
     * @return
     * @throws SQLException
     */
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException ;

    /**
     * 根据源类型和操作的SQL语句分析字段并返回
     * @param source
     * @return
     */
    List<FieldMetaBean> analyseSourceField(ReaderSourceBean source) ;

    /**
     * 禁止读取
     */
    public void destroy() ;
}
