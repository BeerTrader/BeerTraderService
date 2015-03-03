package com.exceptions;

@SuppressWarnings("serial")
public class ObjectMappingException extends Exception {
	private final String message;
    
    public ObjectMappingException(String s) {
        message = "Exception: error mapping string " + s;
    }
    
    @Override
    public String getMessage() {
        return message;
    }

	@Override
	public String toString() {
		return "ObjectMappingException [message" + message + "]";
	}  
}
