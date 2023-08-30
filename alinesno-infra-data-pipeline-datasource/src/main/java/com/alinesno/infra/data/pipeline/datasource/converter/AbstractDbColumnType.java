package com.alinesno.infra.data.pipeline.datasource.converter;


import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;

public abstract class AbstractDbColumnType {

    public abstract String getColumnType(TableField field) ;

}
