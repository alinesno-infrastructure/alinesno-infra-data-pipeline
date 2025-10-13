package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;

public class ReplaceTextTransformer implements DataTransformer {
  private final String field;
  private final String oldText;
  private final String newText;

  public ReplaceTextTransformer(String field, String oldText, String newText) {
    this.field = field; this.oldText = oldText; this.newText = newText;
  }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object v = row.get(field);
    if (v instanceof String) {
      row.put(field, ((String)v).replace(oldText, newText));
    }
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override public String getName(){ return "ReplaceText"; }
}