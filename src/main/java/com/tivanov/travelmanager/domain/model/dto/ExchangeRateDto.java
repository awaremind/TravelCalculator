package com.tivanov.travelmanager.domain.model.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
 * @author Tihomir Ivanov
 * Exchange Rate DTO
 * 
 * 	String error,	
 * 	String base	,
 * 	String date	,
 * 	String error,
 * 	BigDecimal rates	
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
	
	@JsonProperty
	private String error;

	@JsonProperty
	private String base;
	
	@JsonProperty
	private String date;
	
	@JsonProperty
	private Map<String, BigDecimal> rates;

}
