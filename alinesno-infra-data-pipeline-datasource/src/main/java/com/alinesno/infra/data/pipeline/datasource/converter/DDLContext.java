package com.alinesno.infra.data.pipeline.datasource.converter;


import com.alinesno.infra.data.pipeline.datasource.converter.dbtype.*;
import com.alinesno.infra.data.pipeline.datasource.enums.DbType;

import java.util.HashMap;
import java.util.Map;

public class DDLContext {

    private static final Map<String , AbstractDbColumnType> dbMap = new HashMap<>() ;

    static {
        dbMap.put(DbType.MYSQL.getType(), MySQLColumn.getInstance()) ;
        dbMap.put(DbType.CLICKHOUSE.getType(), ClickhouseColumn.getInstance()) ;
        dbMap.put(DbType.DORIS.getType(), DorisColumn.getInstance()) ;
        dbMap.put(DbType.ELASTICSEARCH.getType(), ElasticSearchColumn.getInstance()) ;
        dbMap.put(DbType.ORACLE.getType(), OracleColumn.getInstance()) ;
    }

    public static AbstractDbColumnType getDbType(String dbType){
        return dbMap.get(dbType) ;
    }

}
