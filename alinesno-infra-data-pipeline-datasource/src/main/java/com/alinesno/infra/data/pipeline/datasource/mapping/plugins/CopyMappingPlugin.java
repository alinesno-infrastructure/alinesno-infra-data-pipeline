package com.alinesno.infra.data.pipeline.datasource.mapping.plugins;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.mapping.AbstractMappingPlugin;

import java.util.Map;

public class CopyMappingPlugin extends AbstractMappingPlugin {

    private static final CopyMappingPlugin copyMappingPlugin = new CopyMappingPlugin() ;

    @Override
    public String mappingField(Map<String, Integer> columsMap, String[] fieldValueArr, MappingBean mappingBean) {
        int columnIndex = columsMap.get(mappingBean.getSourceField()) ;
        return fieldValueArr[columnIndex];
    }

    public static AbstractMappingPlugin getInstance() {
        return copyMappingPlugin ;
    }
}
