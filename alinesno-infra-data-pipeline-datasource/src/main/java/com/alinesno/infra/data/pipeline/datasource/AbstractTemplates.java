package com.alinesno.infra.data.pipeline.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractTemplates {

    public KafkaTemplate<String,String> getKafkaTemplates(SinkWriter writer) {

        Map<String , Object> props = new HashMap<>() ;

        String bootstrapServers = writer.getBootstraps();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "0"); // 设置ack参数为0

        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);

        return new KafkaTemplate<>(factory);
    }

    public Connection getDataSource(SourceReader reader){

        String password = reader.getPassword() ;
        String url = reader.getJdbcUrl() ;
        String driver = reader.getDriverClass() ;
        String username = reader.getUsername() ;

        return buildDataSource(driver , url , username , password) ;
    }

    private Connection buildDataSource(String driver, String url, String username, String password) {

        Connection con=null;
        try{
            Class.forName(driver);  //注册数据库驱动
            con = DriverManager.getConnection(url , username , password);  //获取数据库连接
        }catch(Exception e){
            log.error("数据库连接失败:{}", e.getMessage()) ;
        }
        return con;  //返回一个连接
    }

    public Connection getDataSource(SinkWriter writer){

        String password = writer.getPassword() ;
        String url = writer.getJdbcUrl() ;
        String driver = writer.getDriverClass() ;
        String username = writer.getUsername() ;

        return buildDataSource(driver , url , username , password) ;
    }
}
