package com.tivanov.travelmanager.domain.model.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
