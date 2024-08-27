package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * AliyunStorageWriter 类是 ComponentSinkWriter 的子类，用于将数据写入阿里云OSS。
 */
@Component("aliyun" + PipeConstants.WRITER_SUFFIX)
public class AliyunStorageWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(AliyunStorageWriter.class);

    /**
     * 将数据写入阿里云OSS。
     *
     * @param taskInfoDto 任务信息对象
     * @param filterFile  要保存上传的文件
     * @param trans       TransEntity 对象
     * @throws IOException  IO异常
     * @throws SQLException SQL异常
     */
    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException {
        SinkWriter writer = taskInfoDto.getWriter();

        String accessKey = writer.getAccessKey();
        String secretKey = writer.getSecurityKey();
        String endpoint = writer.getEndPoint();
        String bucket = writer.getBucket();
        String ossPath = writer.getOssPath();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);

        try {
            // 上传文件到阿里云OSS
            ossClient.putObject(bucket, ossPath, filterFile);

            log.info("File uploaded successfully to Aliyun OSS: {}", ossPath);
        } catch (Exception e) {
            log.error("Error uploading file to Aliyun OSS: {}", e.getMessage());
            throw new IOException("Error uploading file to Aliyun OSS", e);
        } finally {
            // 关闭OSSClient实例
            ossClient.shutdown();
        }
    }
}
