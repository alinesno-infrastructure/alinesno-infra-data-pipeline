package com.alinesno.infra.data.pipeline.dialect.gbase;

import com.alinesno.infra.data.pipeline.core.provider.ProductFactoryProvider;
import com.alinesno.infra.data.pipeline.core.schema.SourceProperties;
import org.apache.commons.lang3.StringUtils;
import com.alinesno.infra.data.pipeline.dialect.mysql.MysqlMetadataQueryProvider;

import java.util.List;

public class GbaseMetadataQueryProvider extends MysqlMetadataQueryProvider {

  public GbaseMetadataQueryProvider(ProductFactoryProvider factoryProvider) {
    super(factoryProvider);
  }

  @Override
  public void postAppendCreateTableSql(StringBuilder builder, String tblComment, List<String> primaryKeys,
      SourceProperties tblProperties) {
    builder.append("ENGINE=EXPRESS DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin");
    if (StringUtils.isNotBlank(tblComment)) {
      builder.append(String.format(" COMMENT='%s' ", tblComment.replace("'", "\\'")));
    }
  }
}
