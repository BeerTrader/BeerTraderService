package com.unittest.domain;

import static org.junit.Assert.assertEquals;
import java.io.IOException;

import org.junit.Test;

import com.objects.domain.User;
import com.objects.mapping.ObjectManager;

public class UserTest {
	@Test
	public void readUserTest() throws IOException {
		User u = ObjectManager.getInstance().readValue("{\"username\":\"usernameTest\", \"password\":\"passwordTest\"}", User.class);
		assertEquals(u.getUsername(),"usernameTest");
		assertEquals(u.getPassword(),"passwordTest");
	}
}
