package ch.fhnw.wodss.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.service.UserService;

public class UserIntTest extends AbstractIntegrationTest {

	private RestTemplate restTemplate = new TestRestTemplate();

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UserService userService;

	@Test
	public void testUserCRUD() throws JsonProcessingException {
		// CREATE
		Map<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("name", "TestUser");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<String>(objectMapper.writeValueAsString(requestBody),
				requestHeaders);
		User user = restTemplate.postForObject("http://localhost:8080/user", httpEntity, User.class);
		Assert.assertNotNull(user.getId());
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		
		// READ
		user = restTemplate.getForObject("http://localhost:8080/user/{0}", User.class, user.getId());
		Assert.assertNotNull(user);
		Assert.assertEquals("TestUser", user.getName());
		User[] users = restTemplate.getForObject("http://localhost:8080/users", User[].class, new Object[0]);
		Assert.assertEquals(1, users.length);
		
		// UPDATE
		user.setName("TestUser2");
		restTemplate.put("http://localhost:8080/user/{0}", user, user.getId());
		userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser2", userFromDb.getName());
		
		// DELETE
		restTemplate.delete("http://localhost:8080/user/{0}", user.getId());
		userFromDb = userService.getById(user.getId());
		Assert.assertNull(userFromDb);

	}
}