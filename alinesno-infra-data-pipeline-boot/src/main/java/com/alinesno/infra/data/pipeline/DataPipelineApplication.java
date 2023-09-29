package com.alinesno.infra.data.pipeline;

import com.alinesno.infra.common.core.context.SpringContext;
import com.alinesno.infra.common.web.adapter.sso.enable.EnableInfraSsoApi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 集成一个Java开发示例工具
 * @author LuoAnDong
 * @since 2023年8月3日 上午6:23:43
 */
@EnableInfraSsoApi
@MapperScan("com.alinesno.infra.data.pipeline.mapper")
@SpringBootApplication
public class DataPipelineApplication {

	public static <T> void main(String[] args) {
		SpringApplication.run(DataPipelineApplication.class, args);
	}

	@Bean
	public SpringContext getSpringContext(){
		return new SpringContext() ;
	}
}