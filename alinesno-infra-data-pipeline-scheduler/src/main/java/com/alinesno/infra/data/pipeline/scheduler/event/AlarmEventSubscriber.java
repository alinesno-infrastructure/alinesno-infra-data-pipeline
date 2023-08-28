package com.alinesno.infra.data.pipeline.scheduler.event;

import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.service.ITransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AlarmEventSubscriber implements ApplicationListener<AlarmEvent> {

    private static final Logger log = LoggerFactory.getLogger(AlarmEventSubscriber.class) ;

    @Autowired
    private ITransService transService ;

    @Override
    public void onApplicationEvent(AlarmEvent event) {
        log.info("trans count = {}" , event);

        TransEntity trans = transService.getById(event.getTransId()) ;
        trans.setTotalDataCount(event.getTotalCount());
        trans.setProcessDataCount(event.getTransCount());

        transService.update(trans) ;
    }
}