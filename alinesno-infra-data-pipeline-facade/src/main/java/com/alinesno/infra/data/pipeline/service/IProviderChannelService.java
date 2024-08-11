package com.alinesno.infra.data.pipeline.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.pipeline.entity.ProviderChannelEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luoxiaodong
 * @version 1.0.0
 */

public interface IProviderChannelService extends IBaseService<ProviderChannelEntity> {

    /**
     * 判断是否打开此类型
     * @param suffix
     * @return
     */
    boolean isOpenType(String suffix);

    /**
     * 初始化文档结构
     * @param userId
     */
    void initProviderChannel(long userId);

}
