package com.tivanov.travelmanager.domain.model.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
	
	@JsonProperty
	@Getter @Setter
	private String error;

	@JsonProperty
	@Getter @Setter
	private String base;
	
	@JsonProperty
	@Getter @Setter
	private String date;
	
	@JsonProperty
	@Getter @Setter
	private Map<String, BigDecimal> rates;

}
