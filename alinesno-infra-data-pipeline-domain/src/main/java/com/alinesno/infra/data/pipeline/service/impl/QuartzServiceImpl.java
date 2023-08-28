package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.QuartzEntity;
import com.alinesno.infra.data.pipeline.mapper.QuartzMapper;
import com.alinesno.infra.data.pipeline.service.IQuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 定时任务Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Service
public class QuartzServiceImpl extends IBaseServiceImpl<QuartzEntity, QuartzMapper> implements IQuartzService {
    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(QuartzServiceImpl.class);
}
