package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.BuildGitEntity;
import com.alinesno.infra.data.pipeline.mapper.BuildGitMapper;
import com.alinesno.infra.data.pipeline.service.IBuildGitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * gitLab仓库地址信息Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Service
public class BuildGitServiceImpl extends IBaseServiceImpl<BuildGitEntity, BuildGitMapper> implements IBuildGitService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(BuildGitServiceImpl.class);
}