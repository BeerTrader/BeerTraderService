package com.exceptions;

@SuppressWarnings("serial")
public class UserNotAuthorizedException extends Exception {
	private final String message;
    
    public UserNotAuthorizedException(String s) {
        message = "Exception: User with username " + s + " is not authorized!";
    }
    
    @Override
    public String getMessage() {
        return message;
    }

	@Override
	public String toString() {
		return "UserNotFoundException [message" + message + "]";
	}  
}