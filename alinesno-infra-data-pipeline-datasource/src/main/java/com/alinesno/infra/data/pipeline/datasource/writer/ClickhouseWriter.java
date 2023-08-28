package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceWriter;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.FieldMap;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
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
import java.sql.SQLException;
import java.util.List;

@Component("clickhouse" + PipeConstants.WRITER_SUFFIX)
public class ClickhouseWriter extends IDataSourceWriter  {

    private static final Logger log = LoggerFactory.getLogger(ClickhouseWriter.class) ;

    @Autowired
    protected TransEventPublisher transEventPublisher ;

    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransEntity trans) throws IOException, SQLException {

        long count = 0L ;
        long readCount = 0L ;
        LineIterator it = FileUtils.lineIterator(filterFile , "UTF-8");
        TransEvent transEvent = new TransEvent(trans.getId()) ;
        transEvent.setTotalCount(trans.getTotalDataCount() );

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                log.info("-->>>> excel count = {} , line = {}" , count++ , line);
                readCount ++ ;

                if (readCount >= 50000) {

                    transEvent.setTransCount(count);
                    transEventPublisher.publishEvent(transEvent);

                    readCount = 0L ;
                }

            }
        } finally {
            IOUtils.closeQuietly(it);
        }

    }

    /**
     * 生成插入的SQL信息
     * @param fileMap
     * @return
     */
    private String genInsertSQL(List<FieldMap> fileMap) {

        return null ;
    }

    /**
     * 生成预处理数据
     * @param fileMap
     * @param writer
     */
    private void genPreparedStatement(List<FieldMap> fileMap, SinkWriter writer) {

    }

}
