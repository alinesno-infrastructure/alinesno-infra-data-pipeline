package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * MinioReader 类是 ComponentSourceReader 的子类，用于从 MinIO 对象存储中读取数据。
 */
@Component("minio" + PipeConstants.READER_SUFFIX)
public class MinioReader extends ComponentSourceReader {

    private static final Logger log = LoggerFactory.getLogger(MinioReader.class);


    /**
     * 从 MinIO 对象存储中读取数据。
     *
     * @param taskInfoDto  任务信息对象
     * @param jobWorkspace 作业工作目录
     * @param trans        TransEntity 对象
     * @return 读取到的文件
     * @throws SQLException SQL异常
     */
    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransformEntity trans) throws SQLException {
        String endPoint = taskInfoDto.getReader().getEndPoint();
        String accessKey = taskInfoDto.getReader().getAccessKey();
        String secretKey = taskInfoDto.getReader().getSecurityKey();
        String bucket = taskInfoDto.getReader().getBucket();
        String objectName = taskInfoDto.getReader().getOssPath();

        try {
            // 创建MinioClient对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endPoint)
                    .credentials(accessKey, secretKey)
                    .build();

            // 下载文件到本地
            File outputFile = new File(jobWorkspace, objectName);

            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .filename(outputFile.getAbsolutePath())
                            .build()
            );

            // 统计处理的数据量
            long count = 0;
            BufferedReader reader = new BufferedReader(new FileReader(outputFile));
            while (reader.readLine() != null) {
                count++;
            }
            trans.setProcessDataCount(count);

            return outputFile;
        } catch (MinioException | IOException e) {
            throw new SQLException("Error reading data from MinIO", e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 销毁资源。
     */
    @Override
    public void destroy() {
        // 可以在这里释放资源，如关闭MinioClient等
    }
}
