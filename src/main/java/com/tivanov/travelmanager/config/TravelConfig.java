package com.tivanov.travelmanager.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "system.travelconfig")
public class TravelConfig {
	
	@NotNull
	@Getter @Setter
	private String exchangeRateUrl;
	
	@NotNull
	@Getter @Setter
	private String refreshInterval;
	
	@NotNull
	@Getter @Setter
	private String defaultCurrency;
	
	@NotNull
	@Getter @Setter
	private int trasversalDepthLevel;
	
}
