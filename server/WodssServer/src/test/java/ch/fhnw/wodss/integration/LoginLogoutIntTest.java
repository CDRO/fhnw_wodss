package ch.fhnw.wodss.integration;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;

public class LoginLogoutIntTest extends AbstractIntegrationTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testTokenCRUD() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");
		
		// CREATE
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);
		Assert.assertNotNull(token.getId());
		Assert.assertTrue(TokenHandler.validate(token.getId()));
		
		// DELETE
		doDelete("http://localhost:8080/logout", token, Boolean.class);
		Assert.assertFalse(TokenHandler.validate(token.getId()));
	}
}