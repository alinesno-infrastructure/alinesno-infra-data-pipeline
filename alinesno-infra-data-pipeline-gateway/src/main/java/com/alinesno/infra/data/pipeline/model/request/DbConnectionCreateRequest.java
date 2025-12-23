// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.model.request;

import com.alinesno.infra.data.pipeline.common.type.ProductTypeEnum;
import com.alinesno.infra.data.pipeline.entity.DatabaseConnectionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DbConnectionCreateRequest {

  private String name;
  private ProductTypeEnum type;
  private String version;
  private String driver;
  private String address;
  private String port;
  private String databaseName;
  private String characterEncoding;
  private String url;
  private String username;
  private String password;

  public DatabaseConnectionEntity toDatabaseConnection() {
    DatabaseConnectionEntity databaseConnectionEntity = new DatabaseConnectionEntity();
    databaseConnectionEntity.setId(null);
    databaseConnectionEntity.setName(name);
    databaseConnectionEntity.setType(type);
    databaseConnectionEntity.setVersion(version.trim());
    databaseConnectionEntity.setDriver(driver.trim());
    databaseConnectionEntity.setAddress(address.trim());
    databaseConnectionEntity.setPort(port.trim());
    databaseConnectionEntity.setDatabaseName(databaseName.trim());
    databaseConnectionEntity.setCharacterEncoding(characterEncoding.trim());
    databaseConnectionEntity.setUrl(url.trim());
    databaseConnectionEntity.setUsername(username);
    databaseConnectionEntity.setPassword(password);

    return databaseConnectionEntity;
  }

}
