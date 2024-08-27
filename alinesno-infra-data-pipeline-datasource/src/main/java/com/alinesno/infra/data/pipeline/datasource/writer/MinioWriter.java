package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * MinioWriter 类是 ComponentSinkWriter 的子类，用于将数据写入 MinIO。
 */
@Component("minio" + PipeConstants.WRITER_SUFFIX)
public class MinioWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(MinioWriter.class);

    @Autowired
    protected TransEventPublisher transEventPublisher;

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
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException {

        long count = 0L;
        long readCount = 0L;
        StringBuilder txtLine = new StringBuilder();
        LineIterator it = FileUtils.lineIterator(filterFile, "UTF-8");

        TransEvent transEvent = new TransEvent(trans.getId());
        transEvent.setTotalCount(trans.getTotalDataCount());
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

            while (it.hasNext()) {
                String line = it.nextLine();
                log.info("-->>>> excel count = {} , line = {}", count++, line);
                readCount++;

                txtLine.append(line);
                txtLine.append(System.lineSeparator());

                if (readCount >= 50000) {
                    processAndWriteData(txtLine, minioClient, bucket, ossPath);
                    txtLine.setLength(0);

                    transEvent.setTransCount(count);
                    transEventPublisher.publishEvent(transEvent);
                    readCount = 0L;
                }
            }

            if(!txtLine.isEmpty()) {
                processAndWriteData(txtLine, minioClient, bucket, ossPath);
            }

            log.info("File uploaded successfully to MinIO: {}", ossPath);
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new IOException("Error uploading file to MinIO: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(it);
        }
    }

    // 处理和写入数据的方法
    private void processAndWriteData(StringBuilder data, MinioClient minioClient, String bucket, String ossPath) throws Exception{
        byte[] dataBytes = data.toString().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(dataBytes);

        // 使用MinioClient对象上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(ossPath)
                        .stream(inputStream, dataBytes.length, -1)
                        .build()
        );
    }
}
