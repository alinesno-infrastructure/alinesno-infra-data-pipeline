package com.alinesno.infra.data.pipeline;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 集成一个Java开发示例工具
 * @author luoxiaodong
 * @since 1.0.0
 */
@SpringBootApplication
public class DataPipelineApplication {

	public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DataPipelineApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
	}

}