package com.lzj.sync;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author outpaces
 */
@EnableAsync
@EnableRabbit
@EnableConfigurationProperties
@SpringBootApplication
public class SyncDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyncDataApplication.class, args);
	}
}
