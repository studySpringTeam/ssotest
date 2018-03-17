package com.myproject.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

public class UsernameEmptyException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	 
	public UsernameEmptyException() {
		super();
	}
 
	public UsernameEmptyException(String message, Throwable cause) {
		super(message, cause);
	}
 
	public UsernameEmptyException(String message) {
		super(message);
	}
 
	public UsernameEmptyException(Throwable cause) {
		super(cause);
	}
	
}
