package com.tivanov.travelmanager.controller;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.tivanov.travelmanager.domain.model.map.Country;
import com.tivanov.travelmanager.facade.TravelServiceRestDataFacade;

@RestController
@Validated
@RequestMapping(value = "/travel") 
public class TravelController {
	
	@Autowired
	private TravelServiceRestDataFacade serviceFacade;
	
	@PostMapping
	@RequestMapping(value = {"/updaterate", "/update/rate"})
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
		String response = serviceFacade.postRequest(requestString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping
	@RequestMapping("/calculate/raw")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> calculateTravelRaw(@RequestBody @NotNull String requestString) 
			throws JsonMappingException, JsonProcessingException {
		TravelResponseDto response = serviceFacade.postRequestRaw(requestString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping
	@RequestMapping(value = {"/add/country", "/addcountry"})
	public ResponseEntity<?> addCountry(@RequestBody @NotNull String countryString) throws JsonProcessingException  {
		serviceFacade.addCountry(countryString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping
	@RequestMapping(value = {"/add/country/connection", "/addcountry/connection"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> addCountryConnection(@RequestBody @NotNull String connectedCountriesString) throws JsonProcessingException  {
		serviceFacade.addCountryConnection(connectedCountriesString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	@RequestMapping(value = {"/exchangerates/{baseCurr}", "/rates/{baseCurr}"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllRatesForBaseCurrency(@NotBlank @PathVariable(name="baseCurr") String baseCurr) {
		ExchangeRateDto exchangeRatesToBaseCurr = serviceFacade.getExchangeRateMap(baseCurr);
		return new ResponseEntity<>(exchangeRatesToBaseCurr, HttpStatus.OK);
	}
	
	@GetMapping
	@RequestMapping(value = {"/neighbours/{country}", "/n/{country}"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllNeigbours(@NotBlank @PathVariable(name="country") String country) {
		country = country.toUpperCase();
		Set<String> neighboursSet = serviceFacade.getNeighbours(country);
		return new ResponseEntity<>(neighboursSet, HttpStatus.OK);
	}
	
	@GetMapping
	@RequestMapping(value = {"/country/all", "/allcountries"})
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllCountries() {
		Set<Country> countriesList = serviceFacade.getAllCountries();
		return new ResponseEntity<>(countriesList, HttpStatus.OK);
	}
	
	
}
