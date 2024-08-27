package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.datasource.exception.ReaderSourceException;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Component("kafka" + PipeConstants.READER_SUFFIX)
public class KafkaReader  extends ComponentSourceReader {

    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransformEntity trans) throws SQLException {

        String bootstrapServers = taskInfoDto.getReader().getBootstraps() ;
        String groupId = UUID.randomUUID().toString() ;
        boolean enableAutoCommit = true;
        int maxPollRecords = 1000 ;
        String kafkaTopic = taskInfoDto.getReader().getTopic() ;
        long count = 0L;
        StringBuilder sb = new StringBuilder();
        boolean flag = true;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId != null ? groupId : UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);

        final File file = new File(jobWorkspace + File.separator + "_".concat(UUID.randomUUID().toString()).concat(".csv"));

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props)) {
            consumer.subscribe(Collections.singletonList(kafkaTopic));
            Record oneRecord = null;
            int commitSyncNum = 0;
            int emptyCount = 0;

            while (flag) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {

                    String value = record.value();

                    log.info("-->>>> kafka count = {} , value = {}", count++, value);

                    sb.append(value);
                    sb.append(System.lineSeparator());
                }

                if (!enableAutoCommit) {
                    commitSyncNum++;
                    if (commitSyncNum >= maxPollRecords) {
                        consumer.commitSync();
                        commitSyncNum = 0;
                    }
                }

                // 数据及时落盘
                try {
                    IOUtils.write(sb, new FileOutputStream(file,true), "UTF-8");
                } catch (IOException e) {
                    log.error("正在写盘 [{}] 行时出错:{}", commitSyncNum, e.getMessage());
                    throw new ReaderSourceException(e) ;
                }

                sb.delete(0, sb.length());

                // 若返回的数据records连续100次都是空，则停止监听
                if(records.isEmpty()) {
                    emptyCount++;
                } else {
                    emptyCount = 0;
                }
                if(emptyCount >= 100) {
                    flag = false;
                }

            }
        }

        // 统计处理的数据量
        trans.setProcessDataCount(count);

        return file;
    }

    @Override
    public void destroy() {
    }
}
