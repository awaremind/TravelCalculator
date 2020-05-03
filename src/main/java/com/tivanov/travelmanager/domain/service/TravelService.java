package com.tivanov.travelmanager.domain.service;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivanov.travelmanager.config.TravelConfig;
import com.tivanov.travelmanager.domain.exception.CountryAlreadyExistsException;
import com.tivanov.travelmanager.domain.exception.CountryNotFoundException;
import com.tivanov.travelmanager.domain.exception.InvalidBaseCurrencyException;
import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.dto.TravelRequestDto;
import com.tivanov.travelmanager.domain.model.dto.TravelResponseDto;
import com.tivanov.travelmanager.domain.model.map.CountriesMap;
import com.tivanov.travelmanager.domain.model.map.Country;
import com.tivanov.travelmanager.domain.processor.TravelProcessor;
import com.tivanov.travelmanager.infrastructure.connector.ExchangeRateConnector;

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
	private ObjectMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		return exchangeRates;
	}

	public Set<Country> getNeighbours(String country) {
		return countriesMap.breadthFirstTraversal(country.toUpperCase(), 1);
	}

	public TravelResponseDto processRequest(TravelRequestDto request)  {
		if (request.getCurrency() != null) {
			request.getCurrency().toUpperCase();
		} else {
			request.setCurrency(config.getDefaultCurrency());
		}
		return processor.processRequest(request);
	}

	public void updateRate(ExchangeRateDto exchangeRates) {
		processor.setCurrExchRateMap(exchangeRates);
	}

	public void addCountry(Country country) {
		if (countriesMap.countryExist(country)) {
			throw new CountryAlreadyExistsException();
		}
		countriesMap.addCountry(country);
	}

	public void addCountryConnection(@NotNull Country c1, @NotNull Country c2) {
		if (countriesMap.countryExist(c1) && countriesMap.countryExist(c2)) {
			countriesMap.addCountryConnection(c1, c2);
		} else {
			throw new CountryNotFoundException();
		}
	}

	public Set<Country> findAllCountries() {
		return countriesMap.findAllCountries();
	}
}
