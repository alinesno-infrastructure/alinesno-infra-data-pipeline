package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

@Component("http" + PipeConstants.READER_SUFFIX)
public class HttpReader extends ComponentSourceReader {

    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException {
        String url = taskInfoDto.getReader().getDownloadUrl();
        String fileName = taskInfoDto.getReader().getFilename();

        try {
            // 创建URL对象
            URL fileUrl = new URL(url);

            // 打开URL连接
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();

            // 获取输入流
            InputStream inputStream = connection.getInputStream();

            // 创建目标文件
            File outputFile = new File(jobWorkspace, fileName);

            // 创建输出流
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            // 从输入流读取数据并写入输出流
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 关闭流
            outputStream.close();
            inputStream.close();

            return outputFile;
        } catch (IOException e) {
            throw new SQLException("Error reading data from HTTP", e);
        }
    }

    @Override
    public void destroy() {
        // 可以在这里释放资源，如关闭连接等
    }
}
