package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.api.CheckDbConnectResult;
import com.alinesno.infra.data.pipeline.constants.DbType;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.mapper.ReaderSourceMapper;
import com.alinesno.infra.data.pipeline.service.IReaderSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@Service
public class ReaderSourceServiceImpl extends IBaseServiceImpl<ReaderSourceEntity, ReaderSourceMapper> implements IReaderSourceService {

    @Override
    public CheckDbConnectResult checkDbConnect(ReaderSourceEntity entity) {

        if (entity.getReaderType().equals(DbType.MY_SQL)) {
            return checkMysqlConnect(entity);
        }
        return new CheckDbConnectResult("不支持的数据库类型", "401", false);
    }

    private CheckDbConnectResult checkMysqlConnect(ReaderSourceEntity entity) {
        String url = entity.getReaderUrl();

        try {
            Connection connection = DriverManager.getConnection(
                    StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(url)),
                    StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(entity.getReaderUsername())),
                    StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(entity.getReaderPasswd())));
            connection.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            return CheckDbConnectResult.reject("590");
        }
        return CheckDbConnectResult.accept("200");
    }


}
