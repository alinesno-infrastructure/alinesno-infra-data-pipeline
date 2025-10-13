package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;
import java.util.Random;

public class ClearNullTransformer implements DataTransformer {
  private final Random rand = new Random();
  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    row.entrySet().removeIf(e -> e.getValue() == null);
    row.putIfAbsent("_random", rand.nextInt(100000));
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override
  public String getName() { return "ClearNull"; }
}