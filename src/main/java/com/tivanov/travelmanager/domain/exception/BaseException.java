package com.tivanov.travelmanager.domain.exception;

public abstract class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = -7814050935839582581L;

	public abstract String getErrorCode();
	
}
