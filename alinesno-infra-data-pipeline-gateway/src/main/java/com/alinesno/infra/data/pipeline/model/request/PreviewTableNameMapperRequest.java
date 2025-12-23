package com.alinesno.infra.data.pipeline.model.request;

import com.alinesno.infra.data.pipeline.common.entity.PatternMapper;
import com.alinesno.infra.data.pipeline.common.type.CaseConvertEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PreviewTableNameMapperRequest {

  private Long id;
  private String schemaName;
  private Boolean isInclude;
  private List<String> tableNames;
  private List<PatternMapper> nameMapper;
  private CaseConvertEnum tableNameCase;
}
