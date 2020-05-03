package com.tivanov.travelmanager.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.dto.TravelResponseDto;
import com.tivanov.travelmanager.facade.TravelServiceRestDataFacade;

@RestController
@RequestMapping(value = "/travel") 
public class TravelController {
	
	@Autowired
	private TravelServiceRestDataFacade serviceFacade;
	
	@PostMapping
	@RequestMapping(value = {"/exchangerate", "/rate"})
	public ResponseEntity<?> updateRateByCode(@RequestBody @NotNull String requestString) 
			throws JsonMappingException, JsonProcessingException {
		serviceFacade.updateRate(requestString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping
	@RequestMapping("/calculate")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> calculateTravel(@RequestBody @NotNull String requestString) 
			throws JsonMappingException, JsonProcessingException {
		TravelResponseDto response = serviceFacade.postRequest(requestString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping
	@RequestMapping(value = {"/exchangerates/{baseCurr}", "/rates/{baseCurr}"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllRatesForBaseCurrency(@PathVariable(name="baseCurr") String baseCurr) {
		ExchangeRateDto exchangeRatesToBaseCurr = serviceFacade.getExchangeRateMap(baseCurr);
		return new ResponseEntity<>(exchangeRatesToBaseCurr, HttpStatus.OK);
	}
	
	@PostMapping
	@RequestMapping(value = {"/add/country/{countryCode}", "/addcountry/{countryCode}"})
	public ResponseEntity<?> addCountry(@NotBlank @PathVariable(name="countryCode") String countryCode) {
		serviceFacade.addCountry(countryCode);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping
	@RequestMapping(value = {"/add/country/{cc1}/connection/{cc2}", "/addcountry/{cc1}/connection/{cc2}"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> addCountryConnection(@NotBlank @PathVariable(name="cc1") String cc1, 
												@NotBlank @PathVariable(name="cc2") String cc2) {
		serviceFacade.addCountryConnection(cc1, cc2);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping
	@RequestMapping(value = {"/neighbours/{country}", "/n/{country}"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllNeigbours(@NotBlank @PathVariable(name="country") String country) {
		country = country.toUpperCase();
		Set<String> neighboursSet = serviceFacade.getNeighbours(country);
		return new ResponseEntity<>(neighboursSet, HttpStatus.OK);
	}
	
	
}
