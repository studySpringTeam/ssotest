package com.springconfig.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

public class PasswordEmptyException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	 
	public PasswordEmptyException() {
		super();
	}
 
	public PasswordEmptyException(String message, Throwable cause) {
		super(message, cause);
	}
 
	public PasswordEmptyException(String message) {
		super(message);
	}
 
	public PasswordEmptyException(Throwable cause) {
		super(cause);
	}
	
}
