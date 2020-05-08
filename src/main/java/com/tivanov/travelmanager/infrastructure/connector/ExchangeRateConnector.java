package com.tivanov.travelmanager.infrastructure.connector;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tivanov.travelmanager.config.TravelConfig;
import com.tivanov.travelmanager.domain.exception.InvalidBaseCurrencyException;

@Component
public class ExchangeRateConnector {
	
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
		
		ResponseEntity<?> respEntity;
		try {
			respEntity = getRestTemplate().exchange(exchangeRateUri, HttpMethod.GET, null, String.class);
		} catch (RestClientException e) {
			throw new InvalidBaseCurrencyException();
		}
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
	
}
