package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.api.CheckDbConnectResult;
import com.alinesno.infra.data.pipeline.constants.DbType;
import com.alinesno.infra.data.pipeline.entity.DatasourceEntity;
import com.alinesno.infra.data.pipeline.mapper.DatasourceMapper;
import com.alinesno.infra.data.pipeline.service.IDatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@Service
public class DatasourceServiceImpl extends IBaseServiceImpl<DatasourceEntity, DatasourceMapper> implements IDatasourceService {

    @Override
    public CheckDbConnectResult checkDbConnect(DatasourceEntity entity) {

        switch (entity.getDbType()) {
            case DbType.MY_SQL: {
                return checkMysqlConnect(entity);
            }
            default: {
                return new CheckDbConnectResult("不支持的数据库类型", "401", false);
            }
        }
    }

    private CheckDbConnectResult checkMysqlConnect(DatasourceEntity entity) {
        String url = entity.getJdbcUrl();

        try {
            Connection connection = DriverManager.getConnection(
                    StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(url)),
                    StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(entity.getDbUsername())),
                    StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(entity.getDbPasswd())));
            connection.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            return CheckDbConnectResult.reject("590");
        }
        return CheckDbConnectResult.accept("200");
    }


}
