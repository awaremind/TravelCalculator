package com.tivanov.travelmanager.domain.model.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import com.tivanov.travelmanager.domain.model.map.Country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
 * @author Tihomir Ivanov
 * Response DTO
 * 
 * 	Country originCountry
 *	Map<Country, BigDecimal> currencyPerCountryMap
 *	BigDecimal remainderAmount
 */

@NoArgsConstructor
@AllArgsConstructor
public class TravelResponseDto {
	
	@Getter @Setter
	private Country originCountry;
	
	@Getter @Setter
	private Set<Country> travelToCountries;
	
	@Getter @Setter
	private Map<Country, BigDecimal> currencyPerCountryMap;
	
	@Getter @Setter
	private BigDecimal remainderAmount;
	
	@Getter @Setter
	private int travelCount;
	
}
