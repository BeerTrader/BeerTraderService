package com.exceptions;

@SuppressWarnings("serial")
public class LabelNotFoundException extends Exception {
	private final String message;
    
    public LabelNotFoundException(String s) {
        message = "Exception: Label with name " + s + " does not exist!";
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
	@Override
	public String toString() {
		return "LabelNotFoundException [message" + message + "]";
	}    
}
