package com.alinesno.infra.data.pipeline.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OnlineSqlDataRequest {

  private String script;

  private Integer page;

  private Integer size;
}
