package ch.fhnw.wodss.integration;

import org.junit.Assert;
import org.junit.Test;

import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;

public class TokenIntTest extends AbstractIntegrationTest {

	@Test
	public void testTokenCRUD() throws Exception {
		// CREATE
		Token token = doGet("http://localhost:8080/token", null, Token.class);
		Assert.assertNotNull(token.getId());
		Assert.assertTrue(TokenHandler.validate(token.getId()));
		
		// DELETE
		doDelete("http://localhost:8080/token", token, Boolean.class);
		Assert.assertFalse(TokenHandler.validate(token.getId()));
	}
}