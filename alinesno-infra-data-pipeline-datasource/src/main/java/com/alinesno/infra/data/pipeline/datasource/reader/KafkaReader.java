package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceReader;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

@Component("kafka" + PipeConstants.READER_SUFFIX)
public class KafkaReader  extends ComponentSourceReader {

    private static final Logger log = LoggerFactory.getLogger(KafkaReader.class) ;
    boolean flag = true ;

    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException {

        String bootstrapServers = taskInfoDto.getReader().getBootstraps() ;
        String groupId = UUID.randomUUID().toString() ;
        boolean enableAutoCommit = true;
        int maxPollRecords = 1000 ;
        String kafkaTopic = taskInfoDto.getReader().getTopic() ;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId != null ? groupId : UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);


        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props)) {
            consumer.subscribe(Collections.singletonList(kafkaTopic));
            Record oneRecord = null;
            int commitSyncNum = 0;

            while (flag) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {

                    String value = record.value();
                }

                if (!enableAutoCommit) {
                    commitSyncNum++;
                    if (commitSyncNum >= maxPollRecords) {
                        consumer.commitSync();
                        commitSyncNum = 0;
                    }
                }

            }
        }


        return null;
    }

    @Override
    public void destroy() {
       this.flag = false ;
    }
}
