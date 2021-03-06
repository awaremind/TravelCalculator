package com.tivanov.travelmanager.domain.processor;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tivanov.travelmanager.config.TravelConfig;
import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.dto.TravelRequestDto;
import com.tivanov.travelmanager.domain.model.dto.TravelResponseDto;
import com.tivanov.travelmanager.domain.model.map.CountriesMap;
import com.tivanov.travelmanager.domain.model.map.Country;

import lombok.Getter;
import lombok.Setter;

@Component
public class TravelProcessor {
	
	private BigDecimal totalBudget;
	private BigDecimal countryBudget;
	private Set<Country> neighbours;
	private String requestCurrency;
	
	@Autowired
	private TravelConfig config;

	@Autowired
	private CountriesMap countriesMap;

	@Getter @Setter
	private ExchangeRateDto currExchRateMap = new ExchangeRateDto();
	
	private BigDecimal calculateRemainder() {
		BigDecimal allCountriesBudgetSum = countryBudget.multiply(new BigDecimal(neighbours.size()));
		return totalBudget.remainder(allCountriesBudgetSum);
	}
	
	private int calculateTravelCount() {
		BigDecimal allCountriesBudgetSum = countryBudget.multiply(new BigDecimal(neighbours.size()));
		BigDecimal count = totalBudget.subtract(calculateRemainder()).divide(allCountriesBudgetSum);
		return count.intValue();
	}
	
	private Map<Country, BigDecimal> calcAllBudgetsInCurrency() {
		Map<Country, BigDecimal> amountInCurrency = new HashMap<>();
		if (currExchRateMap.getRates() == null) {
			currExchRateMap.setRates(new HashMap<>());
		}
		neighbours.forEach(c -> {
			BigDecimal rate = currExchRateMap.getRates().get(c.getCurrency());
			Country tempCountry = new Country(c);
			if (rate == null) {
				rate = new BigDecimal(1);
				tempCountry.setCurrency(requestCurrency); // resetting the currencyCode where no rate is present
			}
			amountInCurrency.put(tempCountry, rate.multiply(countryBudget)
					.multiply(new BigDecimal(calculateTravelCount())));	
		});
		return amountInCurrency;
	}

	public Entry<String, BigDecimal> getRateByCode(String code) {
		return new AbstractMap.SimpleEntry<>(code, currExchRateMap.getRates().get(code));
	}

	public TravelResponseDto processRequest(TravelRequestDto request) {
		setLocalVariablesFromRequest(request);
		
		neighbours = countriesMap.breadthFirstTraversal(request.getOriginCountry(), config.getTrasversalDepthLevel());
		
		TravelResponseDto response = new TravelResponseDto();
		response.setRemainderAmount(calculateRemainder());
		response.setTravelCount(calculateTravelCount());
		
		Country originCountry = countriesMap.findCountryByCode(request.getOriginCountry());
		originCountry.setCurrency(requestCurrency); // storing the request currency overwriting the original currency
		response.setOriginCountry(originCountry);
		
		response.setCurrencyPerCountryMap(calcAllBudgetsInCurrency());
		
		return response;
	}

	private void setLocalVariablesFromRequest(TravelRequestDto request) {
		this.totalBudget = request.getTotalAmount();
		this.countryBudget = request.getAmountPerCountry();
		this.requestCurrency = request.getCurrency();
	}
	
	
}
