package com.tivanov.travelmanager.domain.exception;

public class InvalidBaseCurrencyException extends BaseException {

	private static final long serialVersionUID = -6127256773101071967L;

	@Override
	public String getErrorCode() {
		return "INVALID_BASE_CURRENCY_CODE";
	}

}
