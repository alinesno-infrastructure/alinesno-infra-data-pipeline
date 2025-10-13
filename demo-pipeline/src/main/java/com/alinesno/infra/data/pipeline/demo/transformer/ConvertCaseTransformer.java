package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;

public class ConvertCaseTransformer implements DataTransformer {
  public enum Mode { UPPER, LOWER }
  private final String field;
  private final Mode mode;

  public ConvertCaseTransformer(String field, Mode mode) { this.field = field; this.mode = mode; }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object v = row.get(field);
    if (v instanceof String) {
      String s = (String) v;
      row.put(field, mode == Mode.UPPER ? s.toUpperCase() : s.toLowerCase());
    }
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override public String getName(){ return "ConvertCase"; }
}