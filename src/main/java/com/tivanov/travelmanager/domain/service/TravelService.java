package com.tivanov.travelmanager.domain.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivanov.travelmanager.config.TravelConfig;
import com.tivanov.travelmanager.domain.exception.CountryNotFoundException;
import com.tivanov.travelmanager.domain.exception.InvalidBaseCurrencyException;
import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.dto.TravelRequestDto;
import com.tivanov.travelmanager.domain.model.dto.TravelResponseDto;
import com.tivanov.travelmanager.domain.model.map.CountriesMap;
import com.tivanov.travelmanager.domain.model.map.Country;
import com.tivanov.travelmanager.domain.processor.TravelProcessor;
import com.tivanov.travelmanager.infrastructure.connector.ExchangeRateConnector;
import com.tivanov.travelmanager.infrastructure.persistence.CountryRepository;

@Service
public class TravelService {
	
	@Autowired
	private TravelConfig config;
	
	@Autowired
	private CountriesMap countriesMap;
	
	@Autowired
	private ExchangeRateConnector connector;

	@Autowired
	private TravelProcessor processor;
	
	@Autowired
	private CountryRepository repo;
	
	@Autowired
	private ObjectMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	private void updateRatesInDb(Map<String, BigDecimal> ratesMap) {
		ratesMap.keySet().forEach(key -> {
			repo.setRateByCurrency(key, ratesMap.get(key));
			});
		repo.flush();
	}
	
	public ExchangeRateDto getExchRateGeneralMap (String baseCurrency) {
		if ("".equals(baseCurrency) || baseCurrency == null) {
			baseCurrency = config.getDefaultCurrency();
		}
		String exchangeRatesGeneralString = connector.getGlobalExchangeRates(baseCurrency.toUpperCase());
		
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		ExchangeRateDto exchangeRates = null;
		try {
			exchangeRates = mapper.readValue(exchangeRatesGeneralString, ExchangeRateDto.class);
		} catch (JsonProcessingException e) {
			logger.info(String.format("Exception caught: %s",  e.getMessage().toString()));
		}
		if (!("".equals(exchangeRates.getError()) || exchangeRates.getError() == null)) {
			throw new InvalidBaseCurrencyException();
		}
		
		logger.info(" --- Updating exchange rates in database is...");
		updateRatesInDb(exchangeRates.getRates());
		
		return exchangeRates;
	}

	public Set<Country> getNeighbours(String country) {
		return countriesMap.breadthFirstTraversal(country, 1);
	}

	public TravelResponseDto processRequest(TravelRequestDto request)  {
		if (request.getCurrency() != null) {
			request.getCurrency().toUpperCase();
		} else {
			request.setCurrency(config.getDefaultCurrency());
		}
		return processor.processRequest(request);
	}

	public void updateRate(Country country) {
		repo.setRateByCurrency(country.getCode(), country.getRate());
	}

	public Country findByCurrency(String requestCurrency) {
		return repo.findByCurrency(requestCurrency.toUpperCase());
	}

	public void addCountry(Country country) {
		Country countryInDb = repo.findByCode(country.getCode());
		if (countryInDb != null) {
			country = countryInDb;
		} else {
			throw new CountryNotFoundException();
		}
		countriesMap.addCountry(country);
	}

	public void addCountryConnection(@NotBlank String cc1, @NotBlank String cc2) {
		if (missingInDb(cc1) || missingInDb(cc2)) {
			throw new CountryNotFoundException();
		}
		countriesMap.addEdge(cc1, cc2);
	}

	private boolean missingInDb(String countryCode) {
		Country countryInDb = repo.findByCode(countryCode);
		return countryInDb == null;
	}
}
