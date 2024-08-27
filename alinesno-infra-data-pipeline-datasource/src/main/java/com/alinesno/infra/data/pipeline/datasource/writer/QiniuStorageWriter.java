package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * QiniuStorageWriter 类是 ComponentSinkWriter 的子类，用于将数据写入七牛云存储。
 */
@Component("qiniu" + PipeConstants.WRITER_SUFFIX)
public class QiniuStorageWriter extends ComponentSinkWriter {

    private static final Logger log = LoggerFactory.getLogger(QiniuStorageWriter.class);

    /**
     * 将数据写入七牛云存储。
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
        String bucket = writer.getBucket();
        String ossPath = writer.getOssPath();

        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2()); // TODO 增强拓展性，保证不同地域的存储空间 Region 都能正常运行
        // ...其他参数参考类注释

        // ...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        // 创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        try {
            // 调用 UploadManager 的 put 方法进行文件上传
            uploadManager.put(filterFile.getAbsolutePath(), ossPath, upToken);

            log.info("File uploaded successfully to Qiniu: {}", ossPath);
        } catch (Exception e) {
            log.error("Error uploading file to Qiniu", e);
            throw new IOException("Error uploading file to Qiniu: " + e.getMessage());
        }
    }
}
