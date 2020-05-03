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

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelResponseDto {
	
	private Country originCountry;
	
	private Set<Country> travelToCountries;
	
	private Map<Country, BigDecimal> currencyPerCountryMap;
	
	private BigDecimal remainderAmount;
	
	private int travelCount;
	
}
