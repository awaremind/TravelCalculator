package com.tivanov.travelmanager.domain.exception;

public class BaseCurrencyExchangeRateMissmatchException extends BaseException {
	
	private static final long serialVersionUID = 501648929117982533L;

	@Override
	public String getErrorCode() {
		return "BASE_CURRENCY_RATE_MISMATCH";
	}
}
