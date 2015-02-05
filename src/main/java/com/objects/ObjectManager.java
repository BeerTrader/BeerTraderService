package com.objects;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;

public class ObjectManager {

	private static ObjectMapper instance = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);

	private ObjectManager() {		
	}

	public static ObjectMapper getInstance() {    
		return instance;
	}
}
