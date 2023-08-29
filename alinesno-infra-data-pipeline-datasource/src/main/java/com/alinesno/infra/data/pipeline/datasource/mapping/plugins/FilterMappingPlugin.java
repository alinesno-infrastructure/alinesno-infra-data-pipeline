package com.alinesno.infra.data.pipeline.datasource.mapping.plugins;


import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.mapping.AbstractMappingPlugin;

import java.util.Map;

public class FilterMappingPlugin extends AbstractMappingPlugin {

    private static final FilterMappingPlugin copyMappingPlugin = new FilterMappingPlugin() ;

    @Override
    public String mappingField(Map<String, Integer> columsMap, String[] fieldValueArr, MappingBean mappingBean) {

        int columnIndex = columsMap.get(mappingBean.getSourceField()) ;
        return fieldValueArr[columnIndex];
    }

    public static AbstractMappingPlugin getInstance() {
        return copyMappingPlugin ;
    }
}
