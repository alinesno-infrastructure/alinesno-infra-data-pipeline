package com.alinesno.infra.data.pipeline.model.response;

import com.alinesno.infra.data.pipeline.core.schema.IndexDescription;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel("表元数据详情")
public class MetadataTableDetailResponse {

  private String tableName;
  private String schemaName;
  private String remarks;
  private String type;
  private String createSql;
  private List<String> primaryKeys;
  private List<MetadataColumnDetailResponse> columns;
  private List<IndexDescription> indexes;
}
