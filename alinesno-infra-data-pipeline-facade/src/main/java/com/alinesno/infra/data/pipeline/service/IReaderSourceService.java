package com.alinesno.infra.data.pipeline.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.pipeline.api.CheckDbConnectResult;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;

public interface IReaderSourceService extends IBaseService<ReaderSourceEntity> {

    /**
     * 数据库连接校验
     * @param dbListEntity
     * @return
     */
    CheckDbConnectResult checkDbConnect(ReaderSourceEntity dbListEntity);

}