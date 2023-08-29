package com.alinesno.infra.data.pipeline.datasource.mapping.plugins;


import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.mapping.AbstractMappingPlugin;

import java.util.Map;

public class SumMappingPlugin  extends AbstractMappingPlugin {

    private static final SumMappingPlugin sumMappingPlugin = new SumMappingPlugin() ;

    public String mappingField(Map<String, Integer> columsMap, String[] fieldValueArr, MappingBean mappingBean) {
        long sum = 0L ;

        String[] sourceFieldArr = mappingBean.getSourceField().split("\\|") ;

        for(String field : sourceFieldArr){
            int columnIndex = columsMap.get(field) ;
            long value = Long.parseLong(fieldValueArr[columnIndex]);
            sum += value ;
        }

        return sum+"" ;
    }

    public static SumMappingPlugin getInstance() {
        return sumMappingPlugin ;
    }
}
