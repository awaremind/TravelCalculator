package com.tivanov.travelmanager.domain.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Tihomir Ivanov
 * Request DTO
 * 
 * 	Country originCountry,
 * 	BigDecimal amountPerCountry,
 * 	BigDecimal totalAmount,
 * 	String currency	
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestDto {
	
	@NotBlank
	private String originCountry;
	
	@NotNull
	private BigDecimal amountPerCountry;
	
	@NotNull
	@DecimalMin("00.01")
	private BigDecimal totalAmount;
	
	private String currency;
	
	private boolean automaticRateSet;
}
