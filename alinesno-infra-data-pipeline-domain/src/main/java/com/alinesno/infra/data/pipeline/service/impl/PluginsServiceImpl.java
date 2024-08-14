package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.PluginsEntity;
import com.alinesno.infra.data.pipeline.enums.PluginEnum;
import com.alinesno.infra.data.pipeline.mapper.PluginsMapper;
import com.alinesno.infra.data.pipeline.service.IPluginsService;
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
public class PluginsServiceImpl extends IBaseServiceImpl<PluginsEntity, PluginsMapper> implements IPluginsService {

    @Override
    public boolean isOpenType(String suffix) {

        LambdaQueryWrapper<PluginsEntity> wrapper = new LambdaQueryWrapper<>() ;
        wrapper.eq(PluginsEntity::getIsOpen , 1)
                .eq(PluginsEntity::getPluginName, suffix.toUpperCase()) ;

        long count = count(wrapper) ;

        return count > 0 ;
    }

    @Override
    public void initProviderChannel(long userId) {

        List<PluginsEntity> documentTypes = new ArrayList<>();

        for(PluginEnum type : PluginEnum.getAllDocumentTypes()){

            PluginsEntity typeE = new PluginsEntity(
                    type.getIcon(),
                    type.getName(),
                    type.getDesc(), true, 100, false) ;

            typeE.setOperatorId(userId);

            documentTypes.add(typeE) ;
        }

        this.remove(new LambdaQueryWrapper<PluginsEntity>()
                .eq(PluginsEntity::getOperatorId, userId)
                .in(PluginsEntity::getPluginName , PluginEnum.getAllNames())) ;

        saveBatch(documentTypes) ;
    }
}
