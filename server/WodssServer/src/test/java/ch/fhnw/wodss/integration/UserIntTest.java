package ch.fhnw.wodss.integration;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.UserService;

public class UserIntTest extends AbstractIntegrationTest {

	@Autowired
	private UserService userService;

	@SuppressWarnings("unchecked")
	@Test
	public void testUserAuthorizedCRUD() throws Exception {

		JSONObject json = new JSONObject();
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");
		
		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		// CREATE
		json.clear();
		json.put("name", "TestUser");

		User user = doPost("http://localhost:8080/user", token, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertNotNull(user.getId());

		// READ
		user = doGet("http://localhost:8080/user/{0}", token, User.class, userFromDb.getId());
		Assert.assertNotNull(user);
		Assert.assertEquals("TestUser", user.getName());

		// UPDATE
		json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", "TestUser2");
		user = doPut("http://localhost:8080/user/{0}", token, json, User.class, user.getId());
		userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser2", userFromDb.getName());
		Assert.assertEquals("TestUser2", user.getName());

		// DELETE
		doDelete("http://localhost:8080/user/{0}", token, Boolean.class, user.getId());
		userFromDb = userService.getById(user.getId());
		Assert.assertNull(userFromDb);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUserUnauthorizedCRUD() {

		// IVALID TOKEN
		Token token = TokenHandler.register();
		token.setId("asdf-asdf-asdf-asdf");

		JSONObject json = new JSONObject();
		json.put("name", "TestUser");


		try {
			// CREATE
			doPost("http://localhost:8080/user", token, json, User.class);
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// READ
			doGet("http://localhost:8080/user/{0}", token, User.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// UPDATE
			doPut("http://localhost:8080/user/{0}", token, json, User.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// DELETE
			doDelete("http://localhost:8080/user/{0}", token, Boolean.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}