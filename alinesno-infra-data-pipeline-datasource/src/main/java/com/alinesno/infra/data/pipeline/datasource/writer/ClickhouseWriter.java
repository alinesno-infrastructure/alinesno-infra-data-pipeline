package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.datasource.mapping.SQLFieldMapping;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClickhouseWriter 类是 ComponentSinkWriter 的子类，用于将数据写入 Clickhouse 数据库。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Component("clickhouse" + PipeConstants.WRITER_SUFFIX)
public class ClickhouseWriter extends ComponentSinkWriter {

    @Autowired
    protected TransEventPublisher transEventPublisher;

    /**
     * 将数据写入 Clickhouse 数据库。
     *
     * @param taskInfoDto 任务信息对象
     * @param filterFile  要保存上传的文件
     * @param trans       TransEntity 对象
     * @throws IOException  IO异常
     * @throws SQLException SQL异常
     */
    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException {

        List<JSONObject> jsonObjectList = new ArrayList<>();
        long count = 0L;
        long readCount = 0L;

        Map<String, Integer> headerColumsMap = new HashMap<>();
        Connection connection = getDataSource(taskInfoDto.getWriter());

        List<MappingBean> mappingBeans = taskInfoDto.getFieldMap();

        LineIterator it = FileUtils.lineIterator(filterFile, "UTF-8");
        TransEvent transEvent = new TransEvent(trans.getId());
        transEvent.setTotalCount(trans.getTotalDataCount());

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (count == 0) {  // 处理头信息
                    count++;
                    headerColumsMap = HandleColumn(line);
                    continue;
                }

                log.info("-->>>> excel count = {} , line = {}", count++, line);
                readCount++;

                JSONObject jsonObject = SQLFieldMapping.mapping(headerColumsMap, line, mappingBeans);
                jsonObjectList.add(jsonObject);

                if (readCount >= 50000) {

                    // 做监控通知
                    transEvent.setTransCount(count);
                    transEventPublisher.publishEvent(transEvent);

                    // 保存到数据库中
                    saveDb(jsonObjectList, connection, mappingBeans, taskInfoDto.getWriter().getTableName());

                    readCount = 0L;
                    jsonObjectList = new ArrayList<>();
                }

            }

            if (!it.hasNext()) {
                transEvent.setTransCount(count);
                transEventPublisher.publishEvent(transEvent);
                saveDb(jsonObjectList, connection, mappingBeans, taskInfoDto.getWriter().getTableName());
            }

        } finally {
            IOUtils.closeQuietly(it);
            connection.close();
        }

    }

}
