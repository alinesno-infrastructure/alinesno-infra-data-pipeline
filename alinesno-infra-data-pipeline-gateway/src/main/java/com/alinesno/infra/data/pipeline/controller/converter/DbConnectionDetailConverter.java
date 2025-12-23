// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.controller.converter;


import com.alinesno.infra.data.pipeline.common.converter.AbstractConverter;
import com.alinesno.infra.data.pipeline.entity.DatabaseConnectionEntity;
import com.alinesno.infra.data.pipeline.model.response.DbConnectionDetailResponse;

public class DbConnectionDetailConverter extends
        AbstractConverter<DatabaseConnectionEntity, DbConnectionDetailResponse> {

  @Override
  public DbConnectionDetailResponse convert(DatabaseConnectionEntity databaseConnectionEntity) {
    DbConnectionDetailResponse response = new DbConnectionDetailResponse();
    response.setId(databaseConnectionEntity.getId());
    response.setName(databaseConnectionEntity.getName());
    response.setTypeName(databaseConnectionEntity.getType().getName());
    response.setType(databaseConnectionEntity.getType());
    response.setVersion(databaseConnectionEntity.getVersion());
    response.setDriver(databaseConnectionEntity.getDriver());
    response.setAddress(databaseConnectionEntity.getAddress());
    response.setPort(databaseConnectionEntity.getPort());
    response.setDatabaseName(databaseConnectionEntity.getDatabaseName());
    response.setCharacterEncoding(databaseConnectionEntity.getCharacterEncoding());
    response.setUrl(databaseConnectionEntity.getUrl());
    response.setUsername(databaseConnectionEntity.getUsername());
    response.setPassword(databaseConnectionEntity.getPassword());
    response.setCreateTime(databaseConnectionEntity.getCreateTime());
    response.setUpdateTime(databaseConnectionEntity.getUpdateTime());

    return response;
  }
}
