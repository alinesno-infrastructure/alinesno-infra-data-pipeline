package com.alinesno.infra.data.pipeline.demo.base;

import java.util.Map;

public interface DataTransformer {
    Map<String,Object> transform(Map<String,Object> row) throws Exception;
    String getName();
    default Map<String, Object> handleError(Map<String, Object> current, Exception e){
        return current;
    }
}