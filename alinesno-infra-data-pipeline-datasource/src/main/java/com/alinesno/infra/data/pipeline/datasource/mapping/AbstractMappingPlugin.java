package com.alinesno.infra.data.pipeline.datasource.mapping;

import com.alinesno.infra.data.pipeline.datasource.MappingBean;

import java.util.Map;

public abstract class AbstractMappingPlugin {

    public abstract String mappingField(Map<String, Integer> columsMap, String[] fieldValueArr, MappingBean mappingBean) ;

}
