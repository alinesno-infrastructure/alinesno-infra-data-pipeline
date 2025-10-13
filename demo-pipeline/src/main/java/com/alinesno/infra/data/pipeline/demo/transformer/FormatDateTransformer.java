package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformErrorEvent;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FormatDateTransformer implements DataTransformer {
  private final String field;
  private final String targetFormat;

  public FormatDateTransformer(String field, String targetFormat) {
    this.field = field; this.targetFormat = targetFormat;
  }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object v = row.get(field);
    if (v != null) {
      try {
        Date d;
        if (v instanceof Date) d = (Date)v;
        else d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(v));
        String out = new SimpleDateFormat(targetFormat).format(d);
        row.put(field, out);
        EventPublisher.publish(new TransformSuccessEvent(row, getName()));
      } catch (Exception e) {
        EventPublisher.publish(new TransformErrorEvent(row, getName(), e.getMessage()));
      }
    }
    return row;
  }

  @Override public String getName(){ return "FormatDate"; }
}