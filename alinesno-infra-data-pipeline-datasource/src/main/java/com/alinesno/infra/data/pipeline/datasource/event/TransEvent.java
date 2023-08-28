package com.alinesno.infra.data.pipeline.datasource.event;

import org.springframework.context.ApplicationEvent;

/**
 * TransEvent 事件
 */
public class TransEvent extends ApplicationEvent {

    private Long totalCount ;
    private Long transCount;
    private Long transId ;

    public TransEvent(Long transId) {
        super(transId);
        this.transId = transId ;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTransCount() {
        return transCount;
    }

    public void setTransCount(Long transCount) {
        this.transCount = transCount;
    }

    @Override
    public String toString() {
        return "TransEvent{" +
                "totalCount=" + totalCount +
                ", transCount=" + transCount +
                ", transId=" + transId +
                '}';
    }
}