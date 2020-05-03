package com.tivanov.travelmanager.infrastructure.connector;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tivanov.travelmanager.config.TravelConfig;
import com.tivanov.travelmanager.domain.exception.InvalidBaseCurrencyException;

@EnableScheduling
@Component
public class ExchangeRateConnector {
	
	private static final String DEFAULT_CURRENCY = "EUR";

	@Autowired
	private TravelConfig config;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(5000);
		requestFactory.setReadTimeout(5000);
		
		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}
	
	public String getGlobalExchangeRates(String currency) {
		String exchangeRateUriString = config.getExchangeRateUrl() + currency;
		
		URI exchangeRateUri = UriComponentsBuilder.fromHttpUrl(exchangeRateUriString).build()
				.encode()
				.toUri();
		logger.info(String.format(" --- ExchangeRate Url: [%s]", exchangeRateUri.toString()));
		ResponseEntity<?> respEntity = getRestTemplate().exchange(exchangeRateUri, HttpMethod.GET, null, String.class);
		logger.info(String.format(" --- Returned Response: [%s]", respEntity.getStatusCode()));
		String exchangeRatesGeneralString;
		if (respEntity.getStatusCodeValue() == 200 &&
				((respEntity.getBody() != null) || !"".equals(respEntity.getBody()))) { 
			exchangeRatesGeneralString = respEntity.getBody().toString(); 
		} else {
			throw new InvalidBaseCurrencyException();
		}
		
		logger.info(String.format(" --- Returned Data: [%s]", exchangeRatesGeneralString));
		
		return exchangeRatesGeneralString;
	}
	
	public String getGlobalExchangeRates() {
		return getGlobalExchangeRates(DEFAULT_CURRENCY);
	}

}