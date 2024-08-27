package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.TransformMonitorEntity;
import com.alinesno.infra.data.pipeline.mapper.TransMonitorMapper;
import com.alinesno.infra.data.pipeline.service.ITransformMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 转换监控Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Slf4j
@Service
public class TransformMonitorServiceImpl extends IBaseServiceImpl<TransformMonitorEntity, TransMonitorMapper> implements ITransformMonitorService {

}
