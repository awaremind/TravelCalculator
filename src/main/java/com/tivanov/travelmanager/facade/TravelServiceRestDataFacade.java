package com.tivanov.travelmanager.facade;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.dto.TravelRequestDto;
import com.tivanov.travelmanager.domain.model.dto.TravelResponseDto;
import com.tivanov.travelmanager.domain.model.map.Country;
import com.tivanov.travelmanager.domain.service.TravelService;

@Component
public class TravelServiceRestDataFacade {
	
	@Autowired
	private TravelService service;
	
	@Autowired
	private ObjectMapper mapper;

	public ExchangeRateDto getExchangeRateMap(String baseCurrency) {
		return service.getExchRateGeneralMap(baseCurrency);
	}

	public Set<String> getNeighbours(String country) {
		Set<Country> countrySet = service.getNeighbours(country);
		Set<String> setString = countrySet.stream()
				.map(Country::getCode)
				.collect(Collectors.toSet());
		return setString;
	}

	public TravelResponseDto postRequest(@NotNull String requestString) throws JsonProcessingException {
		TravelRequestDto request = mapper.readValue(requestString, TravelRequestDto.class);
		return service.processRequest(request);
		
	}

	public void updateRate(@NotNull String requestString) throws JsonProcessingException {
		Country country = mapper.readValue(requestString, Country.class);
		service.updateRate(country);
		
	}

	public void addCountry(@NotBlank String countryCode) {
		Country country = new Country(countryCode.toUpperCase());
		service.addCountry(country);
	}

	public void addCountryConnection(@NotBlank String cc1, @NotBlank String cc2) {
		service.addCountryConnection(cc1.toUpperCase(), cc2.toUpperCase());
	}
	
}
