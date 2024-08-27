package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.mapper.TransMapper;
import com.alinesno.infra.data.pipeline.service.ITransformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 转换Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Slf4j
@Service
public class TransformServiceImpl extends IBaseServiceImpl<TransformEntity, TransMapper> implements ITransformService {

}
