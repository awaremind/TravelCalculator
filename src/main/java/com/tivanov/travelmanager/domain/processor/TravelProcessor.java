package com.tivanov.travelmanager.domain.processor;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.dto.TravelRequestDto;
import com.tivanov.travelmanager.domain.model.dto.TravelResponseDto;
import com.tivanov.travelmanager.domain.model.map.CountriesMap;
import com.tivanov.travelmanager.domain.model.map.Country;
import com.tivanov.travelmanager.domain.service.TravelService;

import lombok.Getter;
import lombok.Setter;

@Component
public class TravelProcessor {
	private static final int TRAVERSAL_DEPTH_LEVEL = 1;
	
	private BigDecimal totalBudget;
	private BigDecimal countryBudget;
	private Integer neighboursCount;
	private Set<Country> neighbours;
	private String requestCurrency;
	
	@Autowired
	private CountriesMap countriesMap;

	@Autowired
	private TravelService service;
	
	@Getter @Setter
	private ExchangeRateDto currExchRateMap;
	
	private BigDecimal calculateRemainder() {
		BigDecimal allCountriesBudgetSum = countryBudget.multiply(new BigDecimal(neighboursCount));
		return totalBudget.remainder(allCountriesBudgetSum);
	}
	
	private int calculateTravelCount() {
		BigDecimal allCountriesBudgetSum = countryBudget.multiply(new BigDecimal(neighboursCount));
		BigDecimal count = totalBudget.subtract(calculateRemainder()).divide(allCountriesBudgetSum);
		return count.intValue();
	}
	
	private Map<Country, BigDecimal> calcAllBudgetsInCurrency() {
		Map<Country, BigDecimal> amountInCurrency = new HashMap<>();
		neighbours.forEach(c -> {
			BigDecimal rate = c.getRate();
			if (rate == null) {
				rate = new BigDecimal(1);
				c.setCurrency(requestCurrency);
			}
			amountInCurrency.put(c, rate.multiply(countryBudget));	
			
		});
		
		return amountInCurrency;
	}

	public Entry<String, BigDecimal> getRateByCode(String code) {
		return new AbstractMap.SimpleEntry<>(code, currExchRateMap.getRates().get(code));
	}

	public TravelResponseDto processRequest(TravelRequestDto request) {
		setLocalVariablesFromRequest(request);
		
		calculateNeighbours(request);
		
		TravelResponseDto response = new TravelResponseDto();
		response.setRemainderAmount(calculateRemainder());
		response.setTravelCount(calculateTravelCount());
		response.setOriginCountry(request.getOriginCountry());
		response.setCurrencyPerCountryMap(calcAllBudgetsInCurrency());
		response.setTravelToCountries(neighbours);
		
		return response;
	}

	/**
	 * @param request
	 * @return 
	 */
	private void calculateNeighbours(TravelRequestDto request) {
		neighbours = countriesMap.breadthFirstTraversal(request.getOriginCountry().getCode(), TRAVERSAL_DEPTH_LEVEL);
		neighboursCount = neighbours.size();
	}

	private void setLocalVariablesFromRequest(TravelRequestDto request) {
		this.totalBudget = request.getTotalAmount();
		this.countryBudget = request.getAmountPerCountry();
		this.requestCurrency = request.getCurrency();
		this.currExchRateMap = service.getExchRateGeneralMap(requestCurrency);
	}
	
	
}
