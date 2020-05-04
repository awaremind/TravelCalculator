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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tivanov.travelmanager.domain.model.dto.ExchangeRateDto;
import com.tivanov.travelmanager.domain.model.map.Country;
import com.tivanov.travelmanager.facade.TravelServiceRestDataFacade;

/**
 * 
 * @author Tihomir Ivanov
 * Travel Rest Controller 
 * It provides all API endpoints for travel update routes and calculations 
 *
 */

@RestController
@Validated
@RequestMapping(value = "/travel") 
public class TravelController {
	
	@Autowired
	private TravelServiceRestDataFacade serviceFacade;

	/**
	 * 
	 * @param JSON String with  base currency, rates subObject with all the exchane rates
	 * @return Code 201 Created.
	 * @throws JsonMappingException
	 */
	@PostMapping
	@RequestMapping(value = {"/updaterate", "/update/rate"}, method=RequestMethod.POST)
	public ResponseEntity<?> updateRateByCode(@RequestBody @NotNull String requestString) 
			throws JsonMappingException, JsonProcessingException {
		serviceFacade.updateRate(requestString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param JSON String with originCountry, totalBudget, budgetPerCountry, currency, automaticeRateUpdate
	 * @return String Explanation in readable text for the travel.
	 * @throws JsonMappingException
	 */
	@PostMapping
	@RequestMapping(value = "/calculate", method=RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> calculateTravel(@RequestBody @NotNull String requestString) 
			throws JsonProcessingException {
		String response = serviceFacade.postRequest(requestString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 
	 * @param JSON String with country name, code, and currencyCode
	 * @return Code 201 Created.
	 * @throws JsonMappingException
	 */
	@PostMapping
	@RequestMapping(value = {"/add/country", "/addcountry"}, method=RequestMethod.POST)
	public ResponseEntity<?> addCountry(@RequestBody @NotNull String countryString) 
			throws JsonProcessingException  {
		serviceFacade.addCountry(countryString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param JSON Array with two objects with country name, code, and currencyCode
	 * @return Code 201 Created.
	 * @throws JsonMappingException
	 */
	@PostMapping
	@RequestMapping(value = {"/add/country/connection", "/addcountry/connection"}, method=RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> addCountryConnection(@RequestBody @NotNull String connectedCountriesString) 
			throws JsonProcessingException  {
		serviceFacade.addCountryConnection(connectedCountriesString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param baseCurrency - the base currency to which the rates will be requested
	 * @return JSON with the base currency and rates subObject with all exchange rates
	 */
	@GetMapping
	@RequestMapping(value = {"/exchangerates/{baseCurrency}", "/rates/{baseCurrency}"}, method=RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllRatesForBaseCurrency(
				@NotBlank @PathVariable(name="baseCurrency") String baseCurrency) {
		ExchangeRateDto exchangeRatesToBaseCurr = serviceFacade.getExchangeRateMap(baseCurrency);
		return new ResponseEntity<>(exchangeRatesToBaseCurr, HttpStatus.OK);
	}
	
	/**
	 * 
	 * A request that shows all the countries in the country map graph
	 * @return JSON with all countries, their name, code and currencyCode
	 */
	
	@GetMapping
	@RequestMapping(value = {"/country/all", "/allcountries"}, method=RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllCountries() {
		Set<Country> countriesList = serviceFacade.getAllCountries();
		return new ResponseEntity<>(countriesList, HttpStatus.OK);
	}
}
