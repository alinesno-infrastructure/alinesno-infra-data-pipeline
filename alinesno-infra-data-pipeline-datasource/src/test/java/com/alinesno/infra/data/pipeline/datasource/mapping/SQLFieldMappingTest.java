package com.alinesno.infra.data.pipeline.datasource.mapping;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.utils.SystemUtils;
import com.alinesno.infra.data.pipeline.enums.DataMappingEnums;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SQLFieldMappingTest {

    private static final Logger log = LoggerFactory.getLogger(SQLFieldMapping.class) ;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }


    @Test
    void fieldMapping() {

        String columnHeaderLine = "a,b,c,d,e,f,g,h,i,m,j,l,m,n,o,p,t,r,s,b" ;

        List<String> columnList = new ArrayList<>();
        for(int i = 1 ; i < 10 ;i  ++){

            StringBuilder sb = new StringBuilder() ;
            for(int j = 0 ; j < columnHeaderLine.length() ; j ++){
                sb.append(i).append(j).append(",");
            }

            columnList.add(sb.toString()) ;
        }

        Map<String , Integer> columsMap = new HashMap<>() ;

        for(int i = 0 ;i < columnHeaderLine.split(",").length ; i ++){
            columsMap.put(columnHeaderLine.split(",")[i] , i) ;
        }

        StopWatch stopWatch = new StopWatch("func2");
        stopWatch.start("phase1");

        Map<String , String> dictMap = new HashMap<>() ;
        dictMap.put("94" , "北京") ;
        dictMap.put("84" , "南宁") ;

        List<MappingBean> mappingBeans = new ArrayList<>() ;
        mappingBeans.add(new MappingBean("g" , "H" , DataMappingEnums.COPY.getCode())) ;
        mappingBeans.add(new MappingBean("e" , "M" , DataMappingEnums.DICT.getCode() , dictMap)) ;
        mappingBeans.add(new MappingBean("e|g" , "GE" , DataMappingEnums.SUM.getCode())) ;

        for(String line : columnList){
            JSONObject jsonObject = SQLFieldMapping.mapping(columsMap , line , mappingBeans) ;

            log.info("---->>>>>>>>>>>>>>>>>>>>>----");

            for(String targetField : jsonObject.keySet()){
                log.info("targetField = " + targetField + " , value = " + jsonObject.getString(targetField));
            }

        }

        stopWatch.stop();
        log.info("数据映射时间:{}", SystemUtils.formatMilliseconds(stopWatch.getLastTaskTimeMillis()));
    }

    @Test
    void buildPreparedStatementSql() {

        List<MappingBean> mappingBeans = new ArrayList<>() ;
        mappingBeans.add(new MappingBean("g" , "H" , DataMappingEnums.COPY.getCode())) ;
        mappingBeans.add(new MappingBean("e" , "M" , DataMappingEnums.DICT.getCode())) ;
        mappingBeans.add(new MappingBean("e|g" , "GE" , DataMappingEnums.SUM.getCode())) ;

        StringBuilder sb = new StringBuilder();
        String tableName = "test_table" ;

        sb.append("INSERT INTO ")
                .append(tableName)
                .append(" ( ") ;

        for(int i = 0 ; i < mappingBeans.size() ; i ++){
            sb.append(mappingBeans.get(i).getTargetField());
            if(i != mappingBeans.size() - 1){
                sb.append(" , ") ;
            }
        }
        sb.append(" ) ") ;
        sb.append(" VALUES ")
                .append(" ( ") ;

        for(int i = 0 ; i < mappingBeans.size() ; i ++){
            sb.append(" ? ") ;
            if(i != mappingBeans.size() - 1){
                sb.append(",") ;
            }
        }

        sb.append(" ) ") ;

        log.info("insert sql = {}" , sb.toString());
    }

}