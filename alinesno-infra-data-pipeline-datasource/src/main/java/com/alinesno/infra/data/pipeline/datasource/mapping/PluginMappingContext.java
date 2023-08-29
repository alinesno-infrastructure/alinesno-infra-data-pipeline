package com.alinesno.infra.data.pipeline.datasource.mapping;

import com.alinesno.infra.data.pipeline.datasource.mapping.plugins.*;
import com.alinesno.infra.data.pipeline.enums.DataMappingEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件配置服务，使用插件管理和静态类处理，以规避掉OOM的问题
 */
public class PluginMappingContext {

    private static final Map<String , AbstractMappingPlugin> map = new HashMap<>() ;

    static {
        // 字典转换
        map.put(DataMappingEnums.DICT.getCode(), DictMappingPlugin.getInstance()) ;

        // 值复制
        map.put(DataMappingEnums.COPY.getCode(), CopyMappingPlugin.getInstance()) ;

        // 字段求和
        map.put(DataMappingEnums.SUM.getCode(), SumMappingPlugin.getInstance()) ;

        // 添加常量
        map.put(DataMappingEnums.CONST.getCode(), ConstantMappingPlugin.getInstance()) ;

        // 字段过滤
        map.put(DataMappingEnums.FILTER.getCode(), FilterMappingPlugin.getInstance()) ;

        // 增加序列号
        map.put(DataMappingEnums.ROW_NUMBER.getCode(), RowNumberMappingPlugin.getInstance()) ;
    }

    public static AbstractMappingPlugin getByCode(String mappingPlugin) {
        return map.get(mappingPlugin) ;
    }

}
