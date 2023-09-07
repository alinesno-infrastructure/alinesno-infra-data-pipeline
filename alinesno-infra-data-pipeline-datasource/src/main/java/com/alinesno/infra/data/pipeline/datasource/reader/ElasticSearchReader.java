package com.alinesno.infra.data.pipeline.datasource.reader;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.ComponentSourceReader;
import com.alinesno.infra.data.pipeline.datasource.exception.ReaderSourceException;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

@Component("elasticsearch" + PipeConstants.READER_SUFFIX)
public class ElasticSearchReader extends ComponentSourceReader {

    @Override
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException {

        String esHost = taskInfoDto.getReader().getEsHost();
        String esPort = taskInfoDto.getReader().getEsPort();

        // Elasticsearch连接配置
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(esHost, Integer.parseInt(esPort), "http")));

        // 构建查询请求
        SearchRequest searchRequest = new SearchRequest("your_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        try {
            // 执行查询
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 处理查询结果，将数据保存到文件中
            // ...

            // 返回保存的文件
            return new File("");
        } catch (IOException e) {
            // 处理异常
            throw new ReaderSourceException(e);
        } finally {
            // 关闭Elasticsearch客户端连接
            try {
                client.close();
            } catch (IOException e) {
                // 处理异常
            }
        }
    }

    @Override
    public void destroy() {
        // 在销毁方法中进行资源释放等操作
    }
}
