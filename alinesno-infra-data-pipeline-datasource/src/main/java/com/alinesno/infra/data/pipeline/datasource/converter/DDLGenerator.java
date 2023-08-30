package com.alinesno.infra.data.pipeline.datasource.converter;

import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;

import java.util.List;

public class DDLGenerator {

    public static String genDbDDL(List<TableField> tableFields, String dbType, String tableName, String engine, String charset) {
        StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE TABLE `").append(tableName).append("` (\n");

        for(int i = 0 ; i < tableFields.size() ; i ++){
            TableField field = tableFields.get(i) ;

            ddl.append("  `").append(field.getName()).append("` ");
            ddl.append(DDLContext.getDbType(dbType).getColumnType(field));

            if(i != tableFields.size() -1){
                ddl.append(",");
            }

            ddl.append("\n");
        }

        ddl.append(")");

        if (!engine.isEmpty()) {
            ddl.append(" ENGINE=").append(engine);
        }

        if (!charset.isEmpty()) {
            ddl.append(" DEFAULT CHARSET=").append(charset);
        }

        ddl.append(";\n");

        for (TableField field : tableFields) {
            if(field.isPrimaryKey()){
                ddl.append("ALTER TABLE ")
                        .append(tableName)
                        .append(" ADD PRIMARY KEY (")
                        .append(field.getName())
                        .append(");\n");
            }
        }

        return ddl.toString();
    }

}

