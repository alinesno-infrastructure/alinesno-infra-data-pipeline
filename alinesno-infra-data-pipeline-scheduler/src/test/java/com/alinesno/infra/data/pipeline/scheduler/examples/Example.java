/*
 * Copyright (C) Gustav Karlsson
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alinesno.infra.data.pipeline.scheduler.examples;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public abstract class Example {

  protected Logger log = LoggerFactory.getLogger(getClass());

  public abstract void run(DataSource ds);

  public void runWithDatasource() {
//    run(HsqlDatasource.initDatabase());

    String connectionUrl = "jdbc:mysql://localhost:3306/dev_alinesno_infra_data_pipeline_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL" ;
    String username = "root" ;
    String password = "adminer"  ;

    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setURL(connectionUrl) ;
    dataSource.setUser(username);
    dataSource.setPassword(password);

    run(dataSource);
  }

  protected void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}