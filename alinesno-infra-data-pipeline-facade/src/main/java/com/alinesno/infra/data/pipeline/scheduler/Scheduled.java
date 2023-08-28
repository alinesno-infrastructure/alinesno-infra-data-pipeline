package com.alinesno.infra.data.pipeline.scheduler;

import java.time.Instant;

public class Scheduled {
    public final String taskName;
    public final String id;
    public final Instant executionTime;
    public final Object data;

    public String getTaskName() {
        return taskName;
    }

    public String getId() {
        return id;
    }

    public Instant getExecutionTime() {
        return executionTime;
    }

    public Object getData() {
        return data;
    }

    public Scheduled() {
      this(null, null, null, null);
    }

    public Scheduled(String taskName, String id, Instant executionTime, Object data) {
      this.taskName = taskName;
      this.id = id;
      this.executionTime = executionTime;
      this.data = data;
    }

    @Override
    public String toString() {
        return "Scheduled{" +
                "taskName='" + taskName + '\'' +
                ", id='" + id + '\'' +
                ", executionTime=" + executionTime +
                ", data=" + data +
                '}';
    }
}