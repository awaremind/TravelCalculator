package com.tivanov.travelmanager.facade;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

	public TravelResponseDto postRequestRaw(@NotNull String requestString) throws JsonProcessingException {
		TravelRequestDto request = mapper.readValue(requestString, TravelRequestDto.class);
		return service.processRequest(request);
	}
	
	public String postRequest(@NotNull String requestString) throws JsonProcessingException {
		TravelResponseDto response = postRequestRaw(requestString);
		
		int i = response.getTravelToCountries().size();
		Set<Country> travDest = response.getTravelToCountries();
		Map<Country, BigDecimal> currMap = response.getCurrencyPerCountryMap();
		
		StringBuffer sb = new StringBuffer("Bulgaria has ");
		sb.append(i).append(" neighbour countries (");
		travDest.forEach(c -> {
			sb.append(c.getCode()).append(",");
		});
		sb.setLength(sb.length() - 1);
		sb.append(") and the traveler can travel around them ")
			.append(response.getTravelCount())
			.append(" times. He will have ")
			.append(response.getRemainderAmount())
			.append(" ")
			.append(response.getOriginCountry().getCurrency())
			.append(" leftover. ");
		travDest.forEach(c -> {
			sb.append("For ")
				.append(c.getName())
				.append(" he will need to buy ")
				.append(currMap.get(c))
				.append(" ")
				.append(c.getCurrency())
				.append(". ");
		});
		
		return sb.toString();
	}

	public void updateRate(@NotNull String requestString) throws JsonProcessingException {
		ExchangeRateDto exchangeRates = mapper.readValue(requestString, ExchangeRateDto.class);
		service.updateRate(exchangeRates);
	}

	public void addCountry(@NotBlank String countryString) throws JsonProcessingException {
		Country country = mapper.readValue(countryString, Country.class);
		service.addCountry(country);
	}

	public void addCountryConnection(String connectedCountriesString) throws JsonProcessingException {
		List<Country> countries = Arrays.asList(mapper.readValue(connectedCountriesString, Country[].class));
		countries.forEach(c -> {
			c.setCode(c.getCode().toUpperCase());
		});
		service.addCountryConnection(countries.get(0), countries.get(1));
	}

	public Set<Country> getAllCountries() {
		return service.findAllCountries();
	}
	
}
