package com.alinesno.infra.data.pipeline.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据写入
 */
public abstract class IDataSourceWriter {

    public abstract void writerData(TaskInfoDto taskInfoDto, File filterFile, TransEntity trans) throws IOException, SQLException;

    public DataSource getDataSource(SinkWriter writer){

        String password = writer.getPassword() ;
        String url = writer.getJdbcUrl() ;
        String driver = writer.getDriverClass() ;
        String username = writer.getUsername() ;

        Map<String, String> map = new HashMap<>();
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, driver);
        map.put(DruidDataSourceFactory.PROP_URL, url);
        map.put(DruidDataSourceFactory.PROP_USERNAME, username);
        map.put(DruidDataSourceFactory.PROP_PASSWORD, password);

        try {
            return DruidDataSourceFactory.createDataSource(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
