package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * MinioWriter 类是 ComponentSinkWriter 的子类，用于将数据写入 MinIO。
 */
@Component("minio" + PipeConstants.WRITER_SUFFIX)
public class MinioWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(MinioWriter.class);

    /**
     * 将数据写入 MinIO。
     *
     * @param taskInfoDto 任务信息对象
     * @param filterFile  要保存上传的文件
     * @param trans       TransEntity 对象
     * @throws IOException  IO异常
     * @throws SQLException SQL异常
     */
    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransEntity trans) throws IOException, SQLException {

        SinkWriter writer = taskInfoDto.getWriter();

        String endPoint = writer.getEndPoint();
        String accessKey = writer.getAccessKey();
        String securityKey = writer.getSecurityKey();
        String bucket = writer.getBucket();
        String ossPath = writer.getOssPath();

        try {
            // 创建MinioClient对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endPoint)
                    .credentials(accessKey, securityKey)
                    .build();

            // 使用MinioClient对象上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(ossPath)
                            .build()
            );

            log.info("File uploaded successfully to MinIO: {}", ossPath);
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new IOException("Error uploading file to MinIO: " + e.getMessage());
        }
    }
}
