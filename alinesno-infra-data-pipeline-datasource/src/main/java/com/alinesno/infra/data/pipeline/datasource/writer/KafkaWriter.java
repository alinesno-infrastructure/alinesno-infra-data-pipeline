package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Component("kafka"+ PipeConstants.WRITER_SUFFIX)
public class KafkaWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(KafkaWriter.class) ;
    @Autowired
    protected TransEventPublisher transEventPublisher ;

    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransEntity trans) throws IOException, SQLException {

        long count = 0L ;
        long readCount = 0L ;
        LineIterator it = FileUtils.lineIterator(filterFile , "UTF-8");

        TransEvent transEvent = new TransEvent(trans.getId()) ;
        transEvent.setTotalCount(trans.getTotalDataCount() );

        KafkaTemplate<String , String> kafkaTemplate = getKafkaTemplates(taskInfoDto.getWriter()) ;

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                log.info("-->>>> excel count = {} , line = {}" , count++ , line);
                readCount ++ ;

                // Kafka writer
                kafkaTemplate.send("topic" , line) ;

                if (readCount >= 50000) {
                    transEvent.setTransCount(count);
                    transEventPublisher.publishEvent(transEvent);
                    readCount = 0L ;
                }
            }
        } finally {
            kafkaTemplate.flush();
            IOUtils.closeQuietly(it);
        }

    }
}
