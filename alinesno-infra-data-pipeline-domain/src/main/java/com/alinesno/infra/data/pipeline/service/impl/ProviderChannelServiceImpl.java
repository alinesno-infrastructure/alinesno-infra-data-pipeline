package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.ProviderChannelEntity;
import com.alinesno.infra.data.pipeline.enums.ProviderChannelEnum;
import com.alinesno.infra.data.pipeline.mapper.ProviderChannelMapper;
import com.alinesno.infra.data.pipeline.service.IProviderChannelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Service
public class ProviderChannelServiceImpl extends IBaseServiceImpl<ProviderChannelEntity, ProviderChannelMapper> implements IProviderChannelService {

    @Override
    public boolean isOpenType(String suffix) {

        LambdaQueryWrapper<ProviderChannelEntity> wrapper = new LambdaQueryWrapper<>() ;
        wrapper.eq(ProviderChannelEntity::getIsOpen , 1)
                .eq(ProviderChannelEntity::getChannelName, suffix.toUpperCase()) ;

        long count = count(wrapper) ;

        return count > 0 ;
    }

    @Override
    public void initProviderChannel(long userId) {

        List<ProviderChannelEntity> documentTypes = new ArrayList<>();

        for(ProviderChannelEnum type : ProviderChannelEnum.getAllDocumentTypes()){

            ProviderChannelEntity typeE = new ProviderChannelEntity(
                    type.getIcon(),
                    type.getName(),
                    type.getDesc(), true, 100, false) ;

            typeE.setOperatorId(userId);

            documentTypes.add(typeE) ;
        }

        this.remove(new LambdaQueryWrapper<ProviderChannelEntity>()
                .eq(ProviderChannelEntity::getOperatorId, userId)
                .in(ProviderChannelEntity::getChannelName , ProviderChannelEnum.getAllNames())) ;

        saveBatch(documentTypes) ;
    }
}
