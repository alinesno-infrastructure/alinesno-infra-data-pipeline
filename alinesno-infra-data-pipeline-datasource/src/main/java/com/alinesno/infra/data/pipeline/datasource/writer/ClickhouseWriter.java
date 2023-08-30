package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.datasource.mapping.SQLFieldMapping;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * TODO 需要异常数据和断连继传的问题
 */
@Component("clickhouse" + PipeConstants.WRITER_SUFFIX)
public class ClickhouseWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(ClickhouseWriter.class);

    @Autowired
    protected TransEventPublisher transEventPublisher;

    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransEntity trans) throws IOException, SQLException {

        List<JSONObject> jsonObjectList = new ArrayList<>() ;
        long count = 0L;
        long readCount = 0L;

        Map<String , Integer> headerColumsMap = new HashMap<>();
        Connection  connection = getDataSource(taskInfoDto.getWriter()) ;

        List<MappingBean> mappingBeans = taskInfoDto.getFileMap() ;

        LineIterator it = FileUtils.lineIterator(filterFile, "UTF-8");
        TransEvent transEvent = new TransEvent(trans.getId());
        transEvent.setTotalCount(trans.getTotalDataCount());

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (count == 0) {  // 处理头信息
                    count ++ ;
                    headerColumsMap = HandleColumn(line);
                    continue;
                }

                log.info("-->>>> excel count = {} , line = {}", count++, line);
                readCount++;

                JSONObject jsonObject = SQLFieldMapping.mapping(headerColumsMap , line , mappingBeans) ;
                jsonObjectList.add(jsonObject) ;

                if (readCount >= 50000) {

                    // 做监控通知
                    transEvent.setTransCount(count);
                    transEventPublisher.publishEvent(transEvent);

                    // 保存到数据库中
                    saveDb(jsonObjectList , connection , mappingBeans , taskInfoDto.getWriter().getTableName()) ;

                    readCount = 0L;
                    jsonObjectList = new ArrayList<>() ;
                }

            }

            if(!it.hasNext()){
                transEvent.setTransCount(count);
                transEventPublisher.publishEvent(transEvent);
                saveDb(jsonObjectList , connection , mappingBeans , taskInfoDto.getWriter().getTableName()) ;
            }

        } finally {
            IOUtils.closeQuietly(it);
            connection.close();
        }

    }

}
