package com.alinesno.infra.data.pipeline.config;

import com.alinesno.infra.common.facade.enable.EnableActable;
import com.alinesno.infra.common.web.adapter.sso.enable.EnableInfraSsoApi;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@EnableActable
@EnableInfraSsoApi
@MapperScan("com.alinesno.infra.data.pipeline.mapper")
@Configuration
public class AppConfiguration implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }

}
