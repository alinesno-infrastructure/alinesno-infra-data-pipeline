package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Map;

public class RemoveDuplicatesTransformer implements DataTransformer {
  private final String field;
  public RemoveDuplicatesTransformer(String field) { this.field = field; }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object v = row.get(field);
    if (v instanceof String) {
      String s = (String) v;
      String out = Arrays.stream(s.split(","))
              .map(String::trim)
              .distinct()
              .collect(Collectors.joining(","));
      row.put(field, out);
    }
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override public String getName(){ return "RemoveDuplicates"; }
}