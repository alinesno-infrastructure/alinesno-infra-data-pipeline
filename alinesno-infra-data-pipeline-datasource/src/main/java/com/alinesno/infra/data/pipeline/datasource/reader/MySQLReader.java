package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceReader;
import com.alinesno.infra.data.pipeline.datasource.dto.ResultSetMetaInfo;
import com.alinesno.infra.data.pipeline.datasource.event.TransEvent;
import com.alinesno.infra.data.pipeline.datasource.event.TransEventPublisher;
import com.alinesno.infra.data.pipeline.datasource.exception.ReaderSourceException;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component("mysql" + PipeConstants.READER_SUFFIX)
public class MySQLReader extends ComponentSourceReader {

    private static final Logger log = LoggerFactory.getLogger(MySQLReader.class) ;

    @Autowired
    protected TransEventPublisher transEventPublisher ;

    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException {

        String querySql = buildQuerySql(taskInfoDto.getReader()) ;

        Connection connection = getDataSource(taskInfoDto.getReader());

        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(querySql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(Integer.MIN_VALUE);

        ResultSet resultSet = statement.executeQuery(); //期间不会阻塞 直接返回结果行，过多的缓存在驱动内存中

        long count = 1 ;
        int line = 0 ;

        final StringBuilder sb = new StringBuilder(5000000 * 2);
        final File file = new File(jobWorkspace + File.separator + "_".concat(UUID.randomUUID().toString()).concat(".csv"));

        ResultSetMetaInfo info = new ResultSetMetaInfo(resultSet); //缓存基础信息，避免在while循环中反复调用，提高效率
        String[] columnNames = info.getColumnNames();
        int columnCount = info.getColumnCount() ;

        for (int i = 0; i < columnCount; i++) {
            sb.append(columnNames[i]).append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append("\n");

        TransEvent transEvent = new TransEvent(trans.getId()) ;

        while (resultSet.next()) {

            processRow(columnCount, resultSet, sb);
            line++;

            log.debug("查询数据:{} , 指标:{}" , taskInfoDto.getReader().getName() , count ++);

            // 数据及时落盘
            if (line >= 50000) {

                transEvent.setTransCount(count);
                transEvent.setTotalCount(0L);

                transEventPublisher.publishEvent(transEvent);

                try {
                    IOUtils.write(sb, new FileOutputStream(file,true), "UTF-8");
                } catch (IOException e) {
                    log.error("正在写盘 [{}] 行时出错:{}", line, e.getMessage());
                    throw new ReaderSourceException(e) ;
                }
                // reset line & result
                line = 0;
                sb.delete(0, sb.length());
            }
        }

        // 清理小于50000万的数据
        boolean hasNext = resultSet.next() ;
        if(!hasNext){
            try {
                transEvent.setTransCount(count);
                transEvent.setTotalCount(0L);

                transEventPublisher.publishEvent(transEvent);
                IOUtils.write(sb, new FileOutputStream(file,true), "UTF-8");
            } catch (IOException e) {
                log.error("正在写盘 [{}] 行时出错:{}", line, e.getMessage());
            }
            // reset line & result
            sb.delete(0, sb.length());
        }

        // 统计处理的数据量
        trans.setProcessDataCount(count);

        //先开后关原则
        try{
            // NO DOING
            resultSet.close();
            statement.close();
        }finally {
            connection.close();
        }

        return file ;

    }

    @Override
    public void destroy() {

    }

}
