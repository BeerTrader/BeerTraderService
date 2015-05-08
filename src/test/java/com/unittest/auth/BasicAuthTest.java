package com.unittest.auth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.auth.BasicAuth;

public class BasicAuthTest {
	private String login = "c3RldmVuOnRlc3Q=";
	private String token = "MDdEQTZFNkQtNUZBMS00RjgxLTlFOEEtQTA1MkUzMEYyNEUyfHN0ZXZlbnwxNDMwMjU4NzE3NzE4";
	private String decodedToken = "07DA6E6D-5FA1-4F81-9E8A-A052E30F24E2|steven|1430258717718";
	private String username = "steven";
	private String password = "test";
	
	@Test
	public void decodeTest() {
		String[] result = BasicAuth.decode(login);
		assertEquals(result[0],username);
		assertEquals(result[1],password);
	}
	
	@Test
	public void decodeTokenTest() {
		String result = BasicAuth.decodeToken(token);
		String[] values = result.split("\\|");
		assertEquals(values[1],username);
	}
	
	@Test
	public void encodeTokenTest() {
		String result = BasicAuth.encodeToken(decodedToken);
		assertEquals(result,token);
	}
}
