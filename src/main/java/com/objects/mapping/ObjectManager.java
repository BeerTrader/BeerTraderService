package com.objects.mapping;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.exceptions.ObjectMappingException;
import com.objects.domain.User;

public class ObjectManager {

	private static ObjectMapper instance = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);

	private ObjectManager() {		
	}

	public static ObjectMapper getInstance() {    
		return instance;
	}
	
	public static String writeObjectAsString(Object object) throws ObjectMappingException {
		try {
			return ObjectManager.getInstance().writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new ObjectMappingException(e.getMessage());
		} catch (JsonMappingException e) {
			throw new ObjectMappingException(e.getMessage());
		} catch (IOException e) {
			throw new ObjectMappingException(e.getMessage());
		}
	}
	
	public static Object readObjectAsString(String input, Class<?> objectClass) throws ObjectMappingException {
		
		try {
			return ObjectManager.getInstance().readValue(input, objectClass);
		} catch (JsonGenerationException e) {
			throw new ObjectMappingException(e.getMessage());
		} catch (JsonMappingException e) {
			throw new ObjectMappingException(e.getMessage());
		} catch (IOException e) {
			throw new ObjectMappingException(e.getMessage());
		}
	}
}
