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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

/**
 * 
 * @author Tihomir Ivanov
 * Travel Rest Controller 
 * It provides all API endpoints for travel update routes and calculations 
 *
 */
@Api(value = "/travel")
@ApiModel(description = "Main endpoint to access the application functionality")
@RestController
@Validated
@RequestMapping(value = "/travel") 
public class TravelController {
	
	@Autowired
	private TravelServiceRestDataFacade serviceFacade;

	/**
	 * Updates exchange rates manually
	 * 
	 * @param JSON String with  base currency, rates subObject with all the exchane rates
	 * @return Code 201 Created.
	 * @throws JsonMappingException
	 */
	@ApiOperation(value = "Updates exchange rates manually", notes="This service updates exchange rates. "
			+ "It will overwrite all currently existing exchange rates.")
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "The request has succeeded. The data has been stored."),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or more pieces of the requested data has not been found.")
	        })
	@ApiImplicitParam(
			paramType = "body",
		    name =  "JSON",
		    value = "Request JSON with required parameters.",
		    example = "{\n" + 
		    		"    \"base\": \"EUR\",\n" + 
		    		"    \"rates\": {\n" + 
		    		"        \"MKD\": 62.50,\n" + 
		    		"        \"TRL\": 7.70,\n" + 
		    		"        \"RON\": 4.8431,\n" + 	
		    		"        \"BGN\": 1.9558,\n" + 
		    		"        \"USD\": 1.0876,\n" + 
		    		"        \"GBP\": 0.86905,\n" + 
		    		"        \"RSD\": 119.87\n" + 
		    		"    }\n" + 
		    		"}")
	@PostMapping
	@RequestMapping(value = {"/update/rate"}, method=RequestMethod.POST)
	public ResponseEntity<?> updateRateByCode(
			@ApiParam(hidden=true)
			@RequestBody @NotNull String requestString) 
			throws JsonMappingException, JsonProcessingException {
		serviceFacade.updateRate(requestString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * Main functionality of the application.
	 * It makes all the calculations and returns the human readable result.
	 * 
	 * @param JSON String with originCountry, totalBudget, budgetPerCountry, currency, automaticeRateUpdate
	 * @return String Explanation in readable text for the travel.
	 * @throws JsonMappingException
	 */
	@ApiOperation(value = "Calculates the travel", notes="This service calculates the travel. "
			+ "It returns in human readable text the details of the future trip.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The request has succeeded. The data has been stored."),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or more pieces of the requested data has not been found.")
	        })
	@ApiImplicitParam(
			paramType = "body",
		    name =  "JSON",
		    value = "Request JSON with required parameters.",
		    example = "{\n" + 
		    		"   \"originCountry\": \"BG\",\n" + 
		    		"   \"amountPerCountry\": 100,\n" + 
		    		"   \"totalAmount\": 1200,\n" + 
		    		"   \"currency\": \"EUR\",\n" + 
		    		"   \"automaticRateSet\" : \"true\"\n" + 
		    		"}")
	@PostMapping
	@RequestMapping(value = "/calculate", method=RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> calculateTravel(@ApiParam(hidden = true) @RequestBody @NotNull String requestString) 
			throws JsonProcessingException {
		String response = serviceFacade.postRequest(requestString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Add a country to the map
	 * 
	 * @param JSON String with country name, code, and currencyCode
	 * @return Code 201 Created.
	 * @throws JsonMappingException
	 */
	@ApiOperation(value = "Add country to the map", notes="This service adds another country to the map."
			+ " The proper connections should be added to the country in order to be traversed properly "
			+ "from their neighbours.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The request has succeeded. The data has been stored."),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or more pieces of the requested data has not been found.")
	        })
	@ApiImplicitParam(
			paramType = "body",
		    name =  "JSON",
		    value = "Request JSON with required parameters.",
		    example = "{\n" + 
		    		"    \"name\": \"Turkey\",\n" + 
		    		"    \"code\": \"tr\",\n" + 
		    		"    \"currency\": \"TRL\"\n" + 
		    		"}")
	@PostMapping
	@RequestMapping(value = {"/add/country"}, method=RequestMethod.POST)
	public ResponseEntity<?> addCountry(@ApiParam(hidden = true) @RequestBody @NotNull String countryString) 
			throws JsonProcessingException  {
		serviceFacade.addCountry(countryString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * Adds a connection between countries
	 * 
	 * @param JSON Array with two objects with country name, code, and currencyCode
	 * @return Code 201 Created.
	 * @throws JsonMappingException
	 */
	@ApiOperation(value = "Adds a connection between countries", notes="This service adds a connection "
			+ "between countries in order they to be properly traversed as neighbours.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The request has succeeded. The data has been stored."),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or both of the requested countries has not been found.")
	        })
	@ApiImplicitParam(
			paramType = "body",
		    name =  "JSON Array",
		    value = "Request JSON Array with required parameters.",
		    example = "[{\n" + 
		    		"    \"name\": \"Turkey\",\n" + 
		    		"    \"code\": \"tr\",\n" + 
		    		"    \"currency\": \"TRL\"\n" + 
		    		" },\n" + 
		    		" {\n" + 
		    		"    \"name\": \"Armenia\",\n" + 
		    		"    \"code\": \"am\",\n" + 
		    		"    \"currency\": \"AMD\"\n" + 
		    		"}]")
	@PostMapping
	@RequestMapping(value = {"/add/country/connection"}, method=RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> addCountryConnection(@ApiParam(hidden = true) @RequestBody @NotBlank String connectedCountriesString) 
			throws JsonProcessingException  {
		serviceFacade.addCountryConnection(connectedCountriesString);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Gets Exchange Rates from ECB
	 * 
	 * @param baseCurrency - the base currency to which the rates will be requested
	 * @return JSON with the base currency and rates subObject with all exchange rates
	 */
	@ApiOperation(value = "Gets Exchange Rates from ECB", notes="This service gets retrieved exchange rates. "
			+ "It stores them locally and returns JSON with the base currency and rates sub-object with "
			+ "all currency codes with their respective exchange rates.")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "The request has succeeded.", response = ExchangeRateDto.class,
						examples = @Example(value = {@ExampleProperty(value = "'{\n" +  
																			"    \"base\": \"EUR\",\n" + 
																			"    \"rates\": {\n" + 
																			"        \"MKD\": 62.50,\n" + 
																			"        \"TRL\": 7.70,\n" + 
																			"        \"RON\": 4.8431,\n" + 
																			"        \"BGN\": 1.9558,\n" + 
																			"        \"USD\": 1.0876,\n" + 
																			"        \"RSD\": 119.87\n" + 
																			"    }\n'" + 
																			"}", mediaType = "'application/json'")})),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or more pieces of the requested data has not been found.")
	        })
	@RequestMapping(value = {"/rates/{baseCurrency}"}, method=RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllRatesForBaseCurrency(
			    @ApiParam(name =  "Currency String",
					    type = "String",
					    value = "Three letter abbreviation for country currency",
					    example = "EUR", 
					    required = true)
				@NotBlank @PathVariable(name="baseCurrency") String baseCurrency) {
		ExchangeRateDto exchangeRatesToBaseCurr = serviceFacade.getExchangeRateMap(baseCurrency);
		return new ResponseEntity<>(exchangeRatesToBaseCurr, HttpStatus.OK);
	}
	
	/**
	 * Returns all neighbors for the requested country
	 * 
	 * @param countryCode - the country
	 * @return JSON with the names of neighboring countries
	 */
	@ApiOperation(value = "Gets all neighbours for country", notes="This service shows all neighbors of a country. ")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "The request has succeeded.", response = String.class, responseContainer = "List",
						examples = @Example(value = {@ExampleProperty(value = "" + 
																			"}", mediaType = "'application/json'")})),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or more pieces of the requested data has not been found.")
	        })
	@GetMapping
	@RequestMapping(value = {"/neighbours/{country}"}, method=RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllNeigbours(
			@ApiParam(name =  "Country String",
		    type = "String",
		    value = "Two letter abbreviation for country",
		    example = "BG", 
		    required = true)
			@NotBlank @PathVariable(name="country") String country) {
		country = country.toUpperCase();
		Set<String> neighboursSet = serviceFacade.getNeighbours(country);
		return new ResponseEntity<>(neighboursSet, HttpStatus.OK);
	}
	
	/**
	 * Gets all stored countries in the map graph
	 * 
	 * A request that shows all the countries in the country map graph
	 * @return JSON with all countries, their name, code and currencyCode
	 */
	@ApiOperation(value = "Gets all stored countries", notes="This service prints all stored countries.")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "The request has succeeded.", response = Country.class, responseContainer = "Set"),
	        @ApiResponse(code = 400, message = "The request has not been properly formed or formatted."),
	        @ApiResponse(code = 404, message = "One or more pieces of the requested data has not been found.")
	        })
	@GetMapping
	@RequestMapping(value = {"/list/country/all"}, method=RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> getAllCountries() {
		Set<Country> countriesList = serviceFacade.getAllCountries();
		return new ResponseEntity<>(countriesList, HttpStatus.OK);
	}
}
