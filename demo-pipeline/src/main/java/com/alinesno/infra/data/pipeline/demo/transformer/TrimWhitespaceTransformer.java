package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;

public class TrimWhitespaceTransformer implements DataTransformer {
  private final String field;
  public TrimWhitespaceTransformer(String field) { this.field = field; }
  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object v = row.get(field);
    if (v instanceof String) row.put(field, ((String)v).trim());
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }
  @Override public String getName(){ return "TrimWhitespace"; }
}