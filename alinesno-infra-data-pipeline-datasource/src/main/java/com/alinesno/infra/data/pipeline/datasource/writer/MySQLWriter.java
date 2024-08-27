package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * MinioWriter 类是 ComponentSinkWriter 的子类，用于将数据写入 MinIO。
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Component("mysql" + PipeConstants.WRITER_SUFFIX)
public class MySQLWriter extends ComponentSinkWriter {

    @Autowired
    protected TransEventPublisher transEventPublisher;

    /**
     * 将数据写入 MySQL
     *
     * @param taskInfoDto 任务信息对象
     * @param filterFile  要保存上传的文件
     * @param trans       TransEntity 对象
     * @throws IOException  IO异常
     * @throws SQLException SQL异常
     */
    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException {

        log.debug("开始写入数据到MySQL");

    }

}
