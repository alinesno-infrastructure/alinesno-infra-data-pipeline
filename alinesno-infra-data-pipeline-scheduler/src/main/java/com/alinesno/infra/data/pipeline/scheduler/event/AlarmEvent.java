package com.alinesno.infra.data.pipeline.scheduler.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * TransEvent 事件
 */
@ToString
@Setter
@Getter
public class AlarmEvent extends ApplicationEvent {

    private Long totalCount ;
    private Long transCount;
    private Long transId ;
    private String transName ;
    private int step ;

    public AlarmEvent(Long transId) {
        super(transId);
        this.transId = transId ;
    }

}