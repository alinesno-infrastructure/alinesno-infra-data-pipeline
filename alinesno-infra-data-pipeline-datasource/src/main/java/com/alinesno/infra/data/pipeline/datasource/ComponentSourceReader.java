package com.alinesno.infra.data.pipeline.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import com.google.common.base.CharMatcher;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class ComponentSourceReader extends IDataSourceReader {

    public DataSource getDataSource(SourceReader reader){

        String password = reader.getPassword() ;
        String url = reader.getJdbcUrl() ;
        String driver = reader.getDriverClass() ;
        String username = reader.getUsername() ;

        Map<String, String> map = new HashMap<>();
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, driver);
        map.put(DruidDataSourceFactory.PROP_URL, url);
        map.put(DruidDataSourceFactory.PROP_USERNAME, username);
        map.put(DruidDataSourceFactory.PROP_PASSWORD, password);

        map.put(DruidDataSourceFactory.PROP_INITIALSIZE, "50") ;
        map.put(DruidDataSourceFactory.PROP_MINIDLE, "5") ;
        map.put(DruidDataSourceFactory.PROP_MAXACTIVE, "100") ;

        try {
            return DruidDataSourceFactory.createDataSource(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String buildQuerySql(SourceReader reader) {
        return reader.getQuerySql() ;
    }

    /**
     * 大文件：处理单行
     */
    public void processRow(int columnCount, ResultSet rs, StringBuilder sb) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            Object value = JdbcUtils.getResultSetValue(rs, i);
            if(value != null){
                String result = CharMatcher.breakingWhitespace().removeFrom(value.toString()) ;
                sb.append(result).append(",");
            }else{
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1).append("\n");
    }


}
