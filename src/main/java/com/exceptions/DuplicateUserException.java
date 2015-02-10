package com.exceptions;

@SuppressWarnings("serial")
public class DuplicateUserException extends Exception {
	private String message;
    
    public DuplicateUserException(String s) {
        message = "Exception: User with username " + s + " already exists!";
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
	@Override
	public String toString() {
		return "DuplicateUserException [message" + message + "]";
	}    
}
