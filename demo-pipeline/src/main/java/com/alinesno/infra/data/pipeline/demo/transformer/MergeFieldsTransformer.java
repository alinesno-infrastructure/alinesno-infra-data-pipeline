package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;
import java.util.stream.Collectors;

public class MergeFieldsTransformer implements DataTransformer {
  private final String target;
  private final String[] sources;
  private final String sep;

  public MergeFieldsTransformer(String target, String sep, String... sources) {
    this.target = target; this.sep = sep; this.sources = sources;
  }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    String merged = java.util.Arrays.stream(sources)
            .map(s -> row.getOrDefault(s, "").toString())
            .filter(s -> !s.isEmpty())
            .collect(Collectors.joining(sep));
    row.put(target, merged);
    EventPublisher.publish(new TransformSuccessEvent(row, getName()));
    return row;
  }

  @Override public String getName(){ return "MergeFields"; }
}