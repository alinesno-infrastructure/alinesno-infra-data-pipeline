package com.alinesno.infra.data.pipeline.model.request;

import com.alinesno.infra.data.pipeline.common.entity.PatternMapper;
import com.alinesno.infra.data.pipeline.common.type.CaseConvertEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PreviewColumnNameMapperRequest {

  private Long id;
  private String schemaName;
  private String tableName;
  private List<PatternMapper> nameMapper;
  private CaseConvertEnum columnNameCase;
}
