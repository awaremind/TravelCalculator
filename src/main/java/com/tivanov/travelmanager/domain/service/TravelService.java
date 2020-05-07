package com.tivanov.travelmanager.domain.service;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivanov.travelmanager.config.TravelConfig;
import com.tivanov.travelmanager.domain.exception.BaseCurrencyExchangeRateMissmatchException;
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
@Validated
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
	
	public ExchangeRateDto getExchRateGeneralMap (@NotBlank String baseCurrency) {
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
		updateRate(exchangeRates);
		return exchangeRates;
	}
	
	public Set<Country> getNeighbours(String countryCode) {
		return countriesMap.breadthFirstTraversal(countryCode.toUpperCase(), config.getTrasversalDepthLevel());
	}

	public TravelResponseDto processRequest(@Valid TravelRequestDto request)  {
		String baseCurrency = processor.getCurrExchRateMap().getBase();
		String requestCurrency = request.getCurrency();
				request.setCurrency(request.getCurrency().toUpperCase());
		if (request.isAutomaticRateSet()) {
			processor.setCurrExchRateMap(getExchRateGeneralMap(requestCurrency));
		} else {
			if (!(requestCurrency.equalsIgnoreCase(baseCurrency) || 
							baseCurrency == null)) {
					throw new BaseCurrencyExchangeRateMissmatchException();
				}
		}
		
		if (request.getOriginCountry() == null || "".equals(request.getOriginCountry()) 
				|| !countriesMap.countryExist(request.getOriginCountry())) {
			throw new CountryNotFoundException();
		}
		return processor.processRequest(request);
	}

	public void updateRate(@Valid ExchangeRateDto exchangeRates) {
		processor.setCurrExchRateMap(exchangeRates);
	}

	public void addCountry(@Valid Country country) {
		if (countriesMap.countryExist(country)) {
			throw new CountryAlreadyExistsException();
		}
		countriesMap.addCountry(country);
	}

	public void addCountryConnection(@Valid Country country1, @Valid Country country2) {
		if (countriesMap.countryExist(country1) && countriesMap.countryExist(country2)) {
			countriesMap.addCountryConnection(country1, country2);
		} else {
			throw new CountryNotFoundException();
		}
	}

	public Set<Country> findAllCountries() {
		return countriesMap.findAllCountries();
	}
	
	public void remouveCountry(@Valid Country country) {
		if (!countriesMap.countryExist(country)) {
			throw new CountryNotFoundException();
		}
		countriesMap.removeCountry(country);
	}

	public void removeCountryConnection(Country country1, Country country2) {
		if (countriesMap.countryExist(country1) && countriesMap.countryExist(country2)) {
			countriesMap.removeConnection(country1, country2);
		} else {
			throw new CountryNotFoundException();
		}
	}

	public void removeCountry(Country country) {
		if (countriesMap.countryExist(country)) {
			countriesMap.removeCountry(country);
		} else {
			throw new CountryNotFoundException();
		}
		
	}
}
