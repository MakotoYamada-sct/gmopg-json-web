package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

@Configuration
public class GmopgConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * application.properties
	 */
	@Value("${gmo.api.url}")
	@Getter
	@Setter
	private String apiUrl;

	@Value("${gmo.api.pass}")
	@Getter
	@Setter
	private String apiPass;

	@Value("${gmo.shop.id}")
	@Getter
	@Setter
	private String shopId;

	@Value("${gmo.shop.pass}")
	@Getter
	@Setter
	private String shopPass;

	@Value("${gmo.site.id}")
	@Getter
	@Setter
	private String siteId;

	@Value("${gmo.site.pass}")
	@Getter
	@Setter
	private String sitePass;

	@Value("${gmo.shop.order.prefix}")
	@Getter
	@Setter
	private String shopOrderPrefix;

	@Value("${gmo.h2.console.path}")
	@Getter
	@Setter
	private String h2ConsolePath;

}