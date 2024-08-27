package com.alinesno.infra.data.pipeline.datasource.event;

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
public class TransEvent extends ApplicationEvent {

    private long totalCount ;
    private long transCount;
    private long transId ;
    private long jobInstanceId;

    public TransEvent(Long transId) {
        super(transId);
        this.transId = transId ;
    }

}