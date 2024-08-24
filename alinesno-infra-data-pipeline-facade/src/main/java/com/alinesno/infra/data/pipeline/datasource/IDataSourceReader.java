package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.alinesno.infra.data.pipeline.transfer.bean.FieldMetaBean;
import com.alinesno.infra.data.pipeline.transfer.bean.TableMetaBean;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    List<FieldMetaBean> analyseSourceField(SourceReader source) ;

    /**
     * 获取数据
     * @param source
     * @return
     */
    List<Map<String, Object>> fetchData(SourceReader source) ;

    /**
     * 禁止读取
     */
    void destroy() ;

    /**
     * 获取数据源表结构
     * @param reader
     * @return
     */
    List<TableMetaBean> fetchTableData(SourceReader reader);

    /**
     * 连接源验证是否正常
     * @param reader
     */
    void checkConnection(SourceReader reader);

}
