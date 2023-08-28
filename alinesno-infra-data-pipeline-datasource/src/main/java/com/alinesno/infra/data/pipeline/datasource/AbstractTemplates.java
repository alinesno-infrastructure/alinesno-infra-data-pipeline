package com.alinesno.infra.data.pipeline.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTemplates {

    public KafkaTemplate<String,String> getKafkaTemplates(SinkWriter writer) {

        Map<String , Object> props = new HashMap<>() ;

        String bootstrapServers = writer.getBootstraps();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "1"); // 设置ack参数为1

        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);

        return new KafkaTemplate<>(factory);
    }

    public DataSource getDataSource(SourceReader reader){

        String password = reader.getPassword() ;
        String url = reader.getJdbcUrl() ;
        String driver = reader.getDriverClass() ;
        String username = reader.getUsername() ;

        Map<String, String> map = new HashMap<>();
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, driver);
        map.put(DruidDataSourceFactory.PROP_URL, url);
        map.put(DruidDataSourceFactory.PROP_USERNAME, username);
        map.put(DruidDataSourceFactory.PROP_PASSWORD, password);

        map.put(DruidDataSourceFactory.PROP_INITIALSIZE, "50") ;
        map.put(DruidDataSourceFactory.PROP_MINIDLE, "5") ;
        map.put(DruidDataSourceFactory.PROP_MAXACTIVE, "100") ;

        try {
            return DruidDataSourceFactory.createDataSource(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
