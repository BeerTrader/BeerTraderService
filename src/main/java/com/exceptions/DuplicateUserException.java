package com.exceptions;

@SuppressWarnings("serial")
public class DuplicateUserException extends Exception {
	private static final long id = 11;
	private String message;
    
    public DuplicateUserException(String s) {
        message = "Exception: User with username " + s + " alread exists!";
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
	@Override
	public String toString() {
		return "DuplicateUserException [id=" + id + ", message" + message + "]";
	}    
}
