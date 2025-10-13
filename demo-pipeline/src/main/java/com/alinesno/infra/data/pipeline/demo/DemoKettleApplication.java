package com.alinesno.infra.data.pipeline.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class DemoKettleApplication implements CommandLineRunner {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job demoJob;

	public static void main(String[] args) {
		SpringApplication.run(DemoKettleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		jobLauncher.run(demoJob, new org.springframework.batch.core.JobParameters());
	}
}