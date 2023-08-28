package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.data.pipeline.entity.TransRunHisEntity;
import com.alinesno.infra.data.pipeline.mapper.TransRunHisMapper;
import com.alinesno.infra.data.pipeline.service.ITransRunHisService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 数据资产统计Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class TransRunHisServiceImpl extends IBaseServiceImpl<TransRunHisEntity, TransRunHisMapper> implements ITransRunHisService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TransRunHisServiceImpl.class);
}
