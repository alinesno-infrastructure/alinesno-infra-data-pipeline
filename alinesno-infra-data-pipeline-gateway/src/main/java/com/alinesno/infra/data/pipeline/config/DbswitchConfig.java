package com.alinesno.infra.data.pipeline.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Properties;

@Configuration
public class DbswitchConfig {

  @Value("${alinesno.configuration.drivers-base-path}")
  private String driversBasePath;

  public String getDriversBasePath() {
    if (StringUtils.isBlank(driversBasePath)) {
      throw new IllegalArgumentException("Invalid configuration parameter:alinesno.configuration.drivers-base-path");
    }
    if (driversBasePath.endsWith(File.separator)) {
      return driversBasePath.substring(0, driversBasePath.length() - 1);
    }
    return driversBasePath;
  }

  @Bean
  public DatabaseIdProvider getDatabaseIdProvider() {
    DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    Properties props = new Properties();
    props.setProperty("PostgreSQL", "postgresql");
    props.setProperty("MySQL", "mysql");
    props.setProperty("H2", "h2");
    databaseIdProvider.setProperties(props);
    return databaseIdProvider;
  }


}
