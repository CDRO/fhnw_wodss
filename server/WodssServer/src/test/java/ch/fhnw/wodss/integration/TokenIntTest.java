package ch.fhnw.wodss.integration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;

public class TokenIntTest extends AbstractIntegrationTest {

	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testTokenCRUD() throws JsonProcessingException {
		// CREATE
		Token token = restTemplate.postForObject("http://localhost:8080/token", null, Token.class);
		Assert.assertNotNull(token.getId());
		Assert.assertTrue(TokenHandler.validate(token.getId()));
		
		// DELETE
		restTemplate.delete("http://localhost:8080/token/{0}", token.getId());
		Assert.assertFalse(TokenHandler.validate(token.getId()));
	}
}