package com.alinesno.infra.data.pipeline.datasource.event;

import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.entity.TransformMonitorEntity;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import com.alinesno.infra.data.pipeline.service.ITransformMonitorService;
import com.alinesno.infra.data.pipeline.service.ITransformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransEventSubscriber implements ApplicationListener<TransEvent> {

    @Autowired
    private ITransformMonitorService transformMonitorService;

    @Override
    public void onApplicationEvent(TransEvent event) {
        log.info("--->>> 数据读取信息:trans count = {}" , event);

        TransformMonitorEntity trans = transformMonitorService.getById(event.getTransId()) ;
        trans.setTotalDataCount(event.getTotalCount());
        trans.setProcessDataCount(event.getTransCount());

        transformMonitorService.update(trans) ;
    }
}