package com.auth;

import javax.xml.bind.DatatypeConverter;

import org.glassfish.jersey.internal.util.Base64;

/**
 * Allow to encode/decode the authentification
 * @author Deisss (LGPLv3)
 * Implementation taken from https://simplapi.wordpress.com/2013/01/24/jersey-jax-rs-implements-a-http-basic-auth-decoder/
 */
public class BasicAuth {
    private BasicAuth() {};
    /**
     * Decode the basic auth and convert it to array login/password
     * @param auth The string encoded authentification
     * @return The login (case 0), the password (case 1)
     */
    public static String[] decode(String auth) {
        //Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
        auth = auth.replaceFirst("[B|b]asic ", "");
 
        //Decode the Base64 into byte[]
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);
 
        //If the decode fails in any case
        if(decodedBytes == null || decodedBytes.length == 0){
            return new String[0];
        }
 
        //Now we can convert the byte[] into a splitted array :
        //  - the first one is login,
        //  - the second one password
        return new String(decodedBytes).split(":", 2);
    }
    
    public static String decodeToken(String auth) {
    	return Base64.decodeAsString(auth);
    }
    
    public static String encodeToken(String token) {
    	return Base64.encodeAsString(token);
    }
}