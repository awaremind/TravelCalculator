package com.tivanov.travelmanager.domain.exception;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class ExceptionTranslator {
	
	private static final String MALFORMED_REQUEST = "THE REQUEST HAS NOT BEEN FORMED OR FORMATTED CORRECTLY";
	
	@ExceptionHandler({MissingServletRequestParameterException.class, 
		MethodArgumentTypeMismatchException.class,
		JsonMappingException.class,
		JsonProcessingException.class,
		UnexpectedTypeException.class,
		CountryAlreadyExistsException.class,
		ConstraintViolationException.class,
		MissingServletRequestParameterException.class
		})
	public ResponseEntity<Object> IllegalRequestExceptionHandler(final Exception e) {
		return new ResponseEntity<>(MALFORMED_REQUEST, HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler({InvalidBaseCurrencyException.class,
		BaseCurrencyExchangeRateMissmatchException.class})
	public ResponseEntity<Object> badRequestExceptionHandler(final BaseException e) {
		return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({CountryNotFoundException.class,
		EmptyResultDataAccessException.class})
	public ResponseEntity<Object> requestedDataNotFoundExceptionHandler(final BaseException e) {
		return new ResponseEntity<>(e.getErrorCode(), HttpStatus.NOT_FOUND);
	}
	
}
