package com.tivanov.travelmanager.domain.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tivanov.travelmanager.domain.model.map.Country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
 * @author Tihomir Ivanov
 * Request DTO
 * 
 * 	Country originCountry
 * 	BigDecimal amountPerCountry
 * 	BigDecimal totalAmount
 * 	String currency	
 */

@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestDto {
	
	@NotBlank
	@Getter @Setter
	private Country originCountry;
	
	@NotNull
	@Getter @Setter
	private BigDecimal amountPerCountry;
	
	@NotNull
	@Getter @Setter
	private BigDecimal totalAmount;
	
	@Getter @Setter
	private String currency;
}
