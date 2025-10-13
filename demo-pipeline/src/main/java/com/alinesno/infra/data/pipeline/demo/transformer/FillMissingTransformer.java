package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;

public class FillMissingTransformer implements DataTransformer {
  private final String field;
  private final Object fillValue;
  public FillMissingTransformer(String field, Object fillValue) { this.field = field; this.fillValue = fillValue; }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    if (!row.containsKey(field) || row.get(field) == null) row.put(field, fillValue);
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override public String getName(){ return "FillMissing"; }
}