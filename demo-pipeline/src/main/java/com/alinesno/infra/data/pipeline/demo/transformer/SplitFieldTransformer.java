package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;

public class SplitFieldTransformer implements DataTransformer {
  private final String field;
  private final String delimiter;
  private final String[] targetFields;

  public SplitFieldTransformer(String field, String delimiter, String... targetFields) {
    this.field = field; this.delimiter = delimiter; this.targetFields = targetFields;
  }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object v = row.get(field);
    if (v instanceof String) {
      String[] parts = ((String)v).split(delimiter, targetFields.length);
      for (int i = 0; i < targetFields.length; i++) {
        row.put(targetFields[i], i < parts.length ? parts[i] : null);
      }
    }
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override public String getName(){ return "SplitField"; }
}