package com.tivanov.travelmanager.domain.exception;

public class CountryAlreadyExistsException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 501648929117982533L;

	@Override
	public String getErrorCode() {
		return "COUNTRY_ALREADY_EXISTS";
	}

}
