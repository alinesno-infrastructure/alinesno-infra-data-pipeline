package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
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

/**
 * KafkaWriter 类是 ComponentSinkWriter 的子类，用于将数据写入 Kafka。
 */
@Component("kafka" + PipeConstants.WRITER_SUFFIX)
public class KafkaWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(KafkaWriter.class);
    @Autowired
    protected TransEventPublisher transEventPublisher;

    /**
     * 将数据写入 Kafka。
     *
     * @param taskInfoDto 任务信息对象
     * @param filterFile  要保存上传的文件
     * @param trans       TransEntity 对象
     * @throws IOException  IO异常
     * @throws SQLException SQL异常
     */
    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException {

        long count = 0L;
        long readCount = 0L;
        LineIterator it = FileUtils.lineIterator(filterFile, "UTF-8");

        TransEvent transEvent = new TransEvent(trans.getId());
        transEvent.setTotalCount(trans.getTotalDataCount());
        SinkWriter kafkaWriter = taskInfoDto.getWriter();

        KafkaTemplate<String, String> kafkaTemplate = getKafkaTemplates(kafkaWriter);

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                log.info("-->>>> excel count = {} , line = {}", count++, line);
                readCount++;

                // Kafka writer
                kafkaTemplate.send(kafkaWriter.getTopic(), line);

                if (readCount >= 50000) {
                    transEvent.setTransCount(count);
                    transEventPublisher.publishEvent(transEvent);
                    readCount = 0L;
                }
            }
        } finally {
            kafkaTemplate.flush();
            IOUtils.closeQuietly(it);
        }

    }
}
