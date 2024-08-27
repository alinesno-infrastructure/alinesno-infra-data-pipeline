package com.alinesno.infra.data.pipeline.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.pipeline.api.TreeSelectDto;
import com.alinesno.infra.data.pipeline.entity.JobCatalogEntity;

import java.util.List;

/**
 * Prompt指令类型Service接口
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
public interface IJobCatalogService extends IBaseService<JobCatalogEntity> {

    /**
     * 查询出指令类型列表
     * @param promptCatalog
     * @return
     */
    List<JobCatalogEntity> selectCatalogList(JobCatalogEntity promptCatalog);

    /**
     * 保存用户类型
     * @param entity
     */
    void insertCatalog(JobCatalogEntity entity);

    /**
     * 查询类型列表树
     * @return
     */
    List<TreeSelectDto> selectCatalogTreeList();
}