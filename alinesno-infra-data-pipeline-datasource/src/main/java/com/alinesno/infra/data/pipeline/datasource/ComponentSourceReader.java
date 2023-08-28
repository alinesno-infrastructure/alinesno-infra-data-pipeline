package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import com.google.common.base.CharMatcher;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ComponentSourceReader extends AbstractTemplates implements IDataSourceReader {

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
