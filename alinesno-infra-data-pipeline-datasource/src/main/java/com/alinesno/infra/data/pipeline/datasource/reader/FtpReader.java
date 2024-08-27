package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.SQLException;

@Component("ftp" + PipeConstants.READER_SUFFIX)
public class FtpReader  extends ComponentSourceReader {
    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransformEntity trans) throws SQLException {
        return null;
    }

    @Override
    public void destroy() {

    }
}
