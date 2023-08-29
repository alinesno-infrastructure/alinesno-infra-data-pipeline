package com.alinesno.infra.data.pipeline.datasource;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComponentSinkWriter extends AbstractTemplates implements IDataSourceWriter {

    private static final Logger log = LoggerFactory.getLogger(ComponentSinkWriter.class);

    /**
     * 保存到数据库源中
     *
     * @param jsonObjectList
     * @param mappingBeans
     */
    public void saveDb(List<JSONObject> jsonObjectList , Connection connection, List<MappingBean> mappingBeans) {

        try {
            // 关闭自动提交
            connection.setAutoCommit(false);

            // 创建 PreparedStatement
            String preparedStatementSql = buildPreparedStatementSql(mappingBeans) ;
            PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementSql) ;

            // 批量插入数据
            for (JSONObject data : jsonObjectList) {

                int i = 1 ;
                for(String key : data.keySet()){
                    Object value = data.get(key);
                    preparedStatement.setObject(i, value) ;
                    i ++ ;
                }

                preparedStatement.addBatch();
            }

            // 执行批处理
            int[] result = preparedStatement.executeBatch();

            // 提交事务
            connection.commit();

            // 关闭资源
            preparedStatement.close();

        } catch (SQLException e) {

            log.error("数据插入异常:{}" , e.getMessage());
            // 出现异常时回滚事务
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException ex) {
                log.error("数据插入回滚异常:{}" , e.getMessage());
            }
            throw new RuntimeException(e);
        }

    }

    private String buildPreparedStatementSql(List<MappingBean> mappingBeans) {

        StringBuilder sb = new StringBuilder();
        String tableName = "kfinfo" ;

        sb.append("INSERT INTO ")
                .append(tableName)
                .append("(") ;

        for(int i = 0 ; i < mappingBeans.size() ; i ++){
            sb.append(mappingBeans.get(i).getTargetField());
            if(i != mappingBeans.size() - 1){
                sb.append(",") ;
            }
        }
        sb.append(")") ;
        sb.append("VALUES")
                .append("(") ;

        for(int i = 0 ; i < mappingBeans.size() ; i ++){
            sb.append("?") ;
            if(i != mappingBeans.size() - 1){
                sb.append(",") ;
            }
        }

        sb.append(")") ;

        return sb.toString();
    }

    /**
     * 处理头信息
     *
     * @param line
     */
    public Map<String, Integer> HandleColumn(String line) {
        String[] headerColumns = line.split(",");

        Map<String, Integer> headerColumsMap = new HashMap<>();

        for (int i = 0; i < headerColumns.length; i++) {
            headerColumsMap.put(headerColumns[i] , i);
        }


        return headerColumsMap;
    }


}
