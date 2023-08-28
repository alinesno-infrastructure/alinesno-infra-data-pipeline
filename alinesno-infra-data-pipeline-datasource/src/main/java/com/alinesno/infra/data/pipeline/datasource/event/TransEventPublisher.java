package com.alinesno.infra.data.pipeline.datasource.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TransEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(TransEvent e) {
        applicationEventPublisher.publishEvent(e) ;
    }
}