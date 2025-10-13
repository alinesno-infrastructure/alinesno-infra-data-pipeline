package com.alinesno.infra.data.pipeline.demo.transformer;

import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformErrorEvent;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.Map;

/**
 * 将数值字段按指定源区间归一化到目标区间。
 * 示例：
 *   new NormalizeDataTransformer("score", 0, 100) // -> target [0,1]
 *   new NormalizeDataTransformer("score", 0, 100, -1, 1) // -> target [-1,1]
 */
public class NormalizeDataTransformer implements DataTransformer {

  private final String field;
  private final double sourceMin;
  private final double sourceMax;
  private final double targetMin;
  private final double targetMax;
  private final boolean clamp; // 是否在归一化后做裁剪（默认 true）

  /**
   * 使用默认目标区间 [0,1]
   */
  public NormalizeDataTransformer(String field, double sourceMin, double sourceMax) {
    this(field, sourceMin, sourceMax, 0.0, 1.0, true);
  }

  /**
   * 指定源区间与目标区间
   */
  public NormalizeDataTransformer(String field, double sourceMin, double sourceMax,
                                  double targetMin, double targetMax) {
    this(field, sourceMin, sourceMax, targetMin, targetMax, true);
  }

  /**
   * 完整构造（可指定是否裁剪）
   */
  public NormalizeDataTransformer(String field, double sourceMin, double sourceMax,
                                  double targetMin, double targetMax, boolean clamp) {
    this.field = field;
    this.sourceMin = sourceMin;
    this.sourceMax = sourceMax;
    this.targetMin = targetMin;
    this.targetMax = targetMax;
    this.clamp = clamp;
  }

  @Override
  public Map<String, Object> transform(Map<String, Object> row) {
    Object raw = row.get(field);
    if (raw == null) {
      EventPublisher.publish(new TransformErrorEvent(row, getName(), "field is null: " + field));
      return row;
    }

    try {
      double value;
      if (raw instanceof Number) {
        value = ((Number) raw).doubleValue();
      } else {
        // 允许字符串数字
        value = Double.parseDouble(String.valueOf(raw));
      }

      if (Double.compare(sourceMax, sourceMin) == 0) {
        String msg = "sourceMax equals sourceMin, cannot normalize (division by zero)";
        EventPublisher.publish(new TransformErrorEvent(row, getName(), msg));
        return row;
      }

      double ratio = (value - sourceMin) / (sourceMax - sourceMin);
      if (clamp) {
        if (ratio < 0.0) ratio = 0.0;
        if (ratio > 1.0) ratio = 1.0;
      }
      double normalized = targetMin + ratio * (targetMax - targetMin);

      // 若原始为整型并且目标区间几乎为整数范围，可以选择保留类型；这里统一写回 Double
      row.put(field, normalized);

      EventPublisher.publish(new TransformSuccessEvent(row, getName()));
      return row;
    } catch (Exception e) {
      EventPublisher.publish(new TransformErrorEvent(row, getName(), e.getMessage()));
      return row;
    }
  }

  @Override
  public String getName() {
    return "NormalizeData";
  }
}