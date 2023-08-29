package com.alinesno.infra.data.pipeline.datasource.mapping.plugins;

import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.mapping.AbstractMappingPlugin;
import org.springframework.util.StringUtils;

import java.util.Map;

public class DictMappingPlugin  extends AbstractMappingPlugin {

    private static final DictMappingPlugin dictMappingPlugin = new DictMappingPlugin() ;

    public String mappingField(Map<String, Integer> columsMap, String[] fieldValueArr, MappingBean mappingBean) {

        int columnIndex = columsMap.get(mappingBean.getSourceField()) ;
        String value = fieldValueArr[columnIndex];
        String dictValue = mappingBean.getDict().get(value) ;

        return StringUtils.hasLength(dictValue)?dictValue:value ;
    }

    public static DictMappingPlugin getInstance() {
        return dictMappingPlugin;
    }
}
