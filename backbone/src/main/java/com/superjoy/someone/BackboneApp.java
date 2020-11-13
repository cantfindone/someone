package com.superjoy.someone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author Ping
 */
@ComponentScan(basePackages = { "com.fuyun.chain.*" })
@SpringBootApplication
@EnableScheduling
@EnableAuthorizationServer
public class BackboneApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BackboneApp.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BackboneApp.class);
	}
}
