// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dialect.oceanbase;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.core.annotation.Product;
import com.alinesno.infra.data.pipeline.core.features.DefaultProductFeatures;
import com.alinesno.infra.data.pipeline.core.features.ProductFeatures;
import com.alinesno.infra.data.pipeline.core.provider.AbstractFactoryProvider;
import com.alinesno.infra.data.pipeline.core.provider.manage.DefaultTableManageProvider;
import com.alinesno.infra.data.pipeline.core.provider.manage.TableManageProvider;
import com.alinesno.infra.data.pipeline.core.provider.meta.MetadataProvider;
import com.alinesno.infra.data.pipeline.core.provider.query.TableDataQueryProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.AutoCastTableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.sync.TableDataSynchronizeProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.AutoCastTableDataWriteProvider;
import com.alinesno.infra.data.pipeline.core.provider.write.TableDataWriteProvider;
import com.alinesno.infra.data.pipeline.dialect.mysql.MysqlFeatures;
import com.alinesno.infra.data.pipeline.dialect.mysql.MysqlMetadataQueryProvider;
import com.alinesno.infra.data.pipeline.dialect.oracle.OracleMetadataQueryProvider;
import com.alinesno.infra.data.pipeline.dialect.oracle.OracleTableDataSynchronizer;
import com.alinesno.infra.data.pipeline.dialect.oracle.OracleTableDataWriteProvider;
import com.alinesno.infra.data.pipeline.dialect.oracle.OracleTableManageProvider;

@Slf4j
@Product(ProductTypeEnum.OCEANBASE)
public class OceanbaseFactoryProvider extends AbstractFactoryProvider {

  private Boolean isMySqlMode;

  public OceanbaseFactoryProvider(DataSource dataSource) {
    super(dataSource);

    try (Connection connection = getDataSource().getConnection()) {
      this.isMySqlMode = OceanbaseUtils.isOceanBaseUseMysqlMode(connection);
      if (log.isDebugEnabled()) {
        log.debug("#### Target OceanBase is {} Mode ", this.isMySqlMode ? "MySQL" : "Oracle");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ProductFeatures getProductFeatures() {
    return isMySqlMode ? new MysqlFeatures() : new DefaultProductFeatures();
  }

  @Override
  public MetadataProvider createMetadataQueryProvider() {
    MetadataProvider provider = isMySqlMode
        ? new MysqlMetadataQueryProvider(this)
        : new OracleMetadataQueryProvider(this);
    return new OceanbaseMetadataQueryProvider(this, provider);
  }

  @Override
  public TableDataQueryProvider createTableDataQueryProvider() {
    return new OceanbaseTableDataQueryProvider(this, isMySqlMode);
  }

  @Override
  public TableDataWriteProvider createTableDataWriteProvider(boolean useInsert) {
    TableDataWriteProvider provider = isMySqlMode
        ? new AutoCastTableDataWriteProvider(this)
        : new OracleTableDataWriteProvider(this);
    return new OceanbaseTableDataWriteProvider(this, provider);
  }

  @Override
  public TableManageProvider createTableManageProvider() {
    TableManageProvider provider = isMySqlMode
        ? new DefaultTableManageProvider(this)
        : new OracleTableManageProvider(this);
    return new OceanbaseTableManageProvider(this, provider);
  }

  @Override
  public TableDataSynchronizeProvider createTableDataSynchronizeProvider() {
    TableDataSynchronizeProvider provider = isMySqlMode
        ? new AutoCastTableDataSynchronizeProvider(this)
        : new OracleTableDataSynchronizer(this);
    return new OceanbaseTableDataSynchronizer(this, provider);
  }
}
