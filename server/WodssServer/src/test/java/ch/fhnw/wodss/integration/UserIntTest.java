package ch.fhnw.wodss.integration;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.UserService;

public class UserIntTest extends AbstractIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private BoardService boardService;

	@SuppressWarnings("unchecked")
	@Test
	public void testUserAuthorizedCRUD() throws Exception {

		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb.getEmail(), userFromDb.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb = userService.getById(user.getId());
		Assert.assertTrue(userFromDb.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

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

	/**
	 * Tests that a user cannot update another user's profile.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testUnauthorizedUpdate() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser2");
		json.put("email", "email2@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser2", userFromDb2.getName());
		Assert.assertEquals("email2@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		try {
			// UPDATE
			json = new JSONObject();
			json.put("id", user.getId());
			json.put("name", "TestUser2");
			user = doPut("http://localhost:8080/user/{0}", token, json, User.class, user.getId());
			Assert.fail();
		} catch (IOException e) {
			// expected
		}
	}

	/**
	 * Tests that a user cannot delete another user's profile;
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testUnauthorizedDelete() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser2");
		json.put("email", "email2@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser2", userFromDb2.getName());
		Assert.assertEquals("email2@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		try {
			// DELETE
			doDelete("http://localhost:8080/user/{0}", token, Boolean.class, user.getId());
			Assert.fail();
		} catch (IOException e) {
			// expected
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUserUnauthorizedCRUD() {

		User user = UserFactory.getInstance().createUser("test", "test");

		// INVALID TOKEN
		Token token = TokenHandler.register(user);
		token.setId("asdf-asdf-asdf-asdf");

		JSONObject json = new JSONObject();
		json.put("name", "TestUser");

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

	/**
	 * Tests getting all users. This should be only possible if the user who
	 * wants to get all users is also a member of this board.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetUsers() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser2");
		json.put("email", "email2@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser2", userFromDb2.getName());
		Assert.assertEquals("email2@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());

		try {
			doGet("http://localhost:8080/boards", token, board1, User[].class);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			User[] users = doGet("http://localhost:8080/boards", token, board2, User[].class);
			Assert.assertEquals(1, users.length);
			Assert.assertEquals(user2, users[0]);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * Tests getting the user profile. This should only be able to be done by
	 * the same logged in user.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetUser() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser2");
		json.put("email", "email2@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser2", userFromDb2.getName());
		Assert.assertEquals("email2@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		try{
			doGet("http://localhost:8080/user/{0}", token, User.class, user.getId());
			Assert.fail();
		} catch (IOException e){
		} catch (Exception e){
			Assert.fail();
		}
		
		try{
			User aLoadedProfile = doGet("http://localhost:8080/user/{0}", token, User.class, user2.getId());
			Assert.assertEquals(aLoadedProfile.getId(), user2.getId());
		} catch (IOException e){
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
	}
	
	/**
	 * Tests deleting the user profile. This should only be able to be done by
	 * the same logged in user.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteUser() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser2");
		json.put("email", "email2@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser2", userFromDb2.getName());
		Assert.assertEquals("email2@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		try{
			doDelete("http://localhost:8080/user/{0}", token, Boolean.class, user.getId());
			Assert.fail();
		} catch (IOException e){
		} catch (Exception e){
			Assert.fail();
		}
		
		try{
			success = doDelete("http://localhost:8080/user/{0}", token, Boolean.class, user2.getId());
			Assert.assertTrue(success);
		} catch (IOException e){
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
	}
	
	/**
	 * Tests modifying the user profile. This should only be able to be done by
	 * the same logged in user.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testModifyUser() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser");
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser", userFromDb.getName());
		Assert.assertEquals("email@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser2");
		json.put("email", "email2@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser2", userFromDb2.getName());
		Assert.assertEquals("email2@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		user2.setEmail("newEmail@fhnw.ch");
		
		try{
			doPut("http://localhost:8080/user/{0}", token, user2, User.class, user.getId());
			Assert.fail();
		} catch (IOException e){
		} catch (Exception e){
			Assert.fail();
		}
		
		try{
			User modUser = doPut("http://localhost:8080/user/{0}", token, user2, User.class, user2.getId());
			Assert.assertEquals("newEmail@fhnw.ch", modUser.getEmail());
		} catch (IOException e){
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
	}
}