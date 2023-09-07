package com.alinesno.infra.data.pipeline.datasource.mapping;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 数据映射关系输出
 */
public class SQLFieldMapping {

    private static final Logger log = LoggerFactory.getLogger(SQLFieldMapping.class) ;

    public static JSONObject mapping(Map<String , Integer> columsMap, String line , List<MappingBean> mappingList){

       Map<String, Object> orderedMap = new LinkedHashMap<>();

       JSONObject object = new JSONObject(orderedMap) ;
       String[] fieldValueArr = line.split("," , columsMap.size()) ;

        for(MappingBean b : mappingList){
            String value = mappingPluginValue(columsMap , fieldValueArr , b) ;

            String targetField = b.getTargetField() ;
            object.put(targetField , value) ;
        }

        return object ;
    }

    /**
     * 处理mapping value
     *
     * @param columsMap
     * @param fieldValueArr
     * @param mappingBean
     * @return
     */
    private static String mappingPluginValue(Map<String, Integer> columsMap, String[] fieldValueArr, MappingBean mappingBean) {
        AbstractMappingPlugin plugin = PluginMappingContext.getByCode(mappingBean.getMappingPlugin());
        return plugin.mappingField(columsMap , fieldValueArr , mappingBean) ;
    }

}
