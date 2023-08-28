package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.TransMonitorEntity;
import com.alinesno.infra.data.pipeline.mapper.TransMonitorMapper;
import com.alinesno.infra.data.pipeline.service.ITransMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 转换监控Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class TransMonitorServiceImpl extends IBaseServiceImpl<TransMonitorEntity, TransMonitorMapper> implements ITransMonitorService {
    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(TransMonitorServiceImpl.class);
}
