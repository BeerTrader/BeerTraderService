package com.exceptions;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {
	private String message;
    
    public UserNotFoundException(String s) {
        message = "Exception: User with username " + s + " does not exist!";
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