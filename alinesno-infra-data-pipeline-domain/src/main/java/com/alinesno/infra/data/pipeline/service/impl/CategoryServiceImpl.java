package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.CategoryEntity;
import com.alinesno.infra.data.pipeline.mapper.CategoryMapper;
import com.alinesno.infra.data.pipeline.service.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Service
public class CategoryServiceImpl extends IBaseServiceImpl<CategoryEntity, CategoryMapper> implements ICategoryService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
}
