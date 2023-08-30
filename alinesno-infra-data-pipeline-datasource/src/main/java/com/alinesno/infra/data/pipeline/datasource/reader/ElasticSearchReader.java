package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceReader;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.SQLException;

@Component("elasticsearch" + PipeConstants.READER_SUFFIX)
public class ElasticSearchReader  extends ComponentSourceReader {
    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException {
        return null;
    }

    @Override
    public void destroy() {

    }
}
