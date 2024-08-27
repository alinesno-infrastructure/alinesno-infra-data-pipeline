package com.alinesno.infra.data.pipeline.datasource.writer;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSinkWriter;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Component("ftp"+ PipeConstants.WRITER_SUFFIX)
public class FtpWriter extends ComponentSinkWriter {

    @Override
    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException {

    }
}