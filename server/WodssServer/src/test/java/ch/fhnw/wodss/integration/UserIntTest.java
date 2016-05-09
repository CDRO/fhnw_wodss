package ch.fhnw.wodss.integration;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.UserService;

public class UserIntTest extends AbstractIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private BoardService boardService;

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateUser() throws Exception {

		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserL");
		json.put("email", "emailL@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUserL", userFromDb.getName());
		Assert.assertEquals("emailL@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb.getId());
		Assert.assertTrue(success);
		userFromDb = userService.getById(user.getId());
		Assert.assertTrue(userFromDb.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);

		// READ
		user = doGet("http://localhost:8080/user/{0}", token, User.class, userFromDb.getId());
		Assert.assertNotNull(user);
		Assert.assertEquals("TestUserL", user.getName());
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
		json.put("name", "TestUserD");
		json.put("email", "emailD@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUserD", userFromDb.getName());
		Assert.assertEquals("emailD@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserE");
		json.put("email", "emailE@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUserE", userFromDb2.getName());
		Assert.assertEquals("emailE@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb2.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);

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
		json.put("name", "TestUserF");
		json.put("email", "emailF@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUserF", userFromDb.getName());
		Assert.assertEquals("emailF@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserG");
		json.put("email", "emailG@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUserG", userFromDb2.getName());
		Assert.assertEquals("emailG@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb2.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);

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
		json.put("name", "TestUserJ");
		json.put("email", "emailJ@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUserJ", userFromDb.getName());
		Assert.assertEquals("emailJ@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserK");
		json.put("email", "emailK@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUserK", userFromDb2.getName());
		Assert.assertEquals("emailK@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb2.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);

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
		
		try{
			// Test do any request after deletion
			doGet("http://localhost:8080/boards", token, Board[].class);
			Assert.fail();
		} catch (IOException e){
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
	public void testDeleteUserWithBoards() throws Exception {

		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserK");
		json.put("email", "emailK@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUserK", userFromDb2.getName());
		Assert.assertEquals("emailK@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb2.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);
		
		// CREATE BOARD
		json.clear();
		json.put("title", "TestBoard");

		Board board = doPost("http://localhost:8080/board", token, json, Board.class);
		Assert.assertNotNull(board.getId().intValue());
		Board boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard", boardFromDb.getTitle());
		
		try{
			success = doDelete("http://localhost:8080/user/{0}", token, Boolean.class, user2.getId());
			Assert.assertTrue(success);
			board = boardService.getById(boardFromDb.getId());
			Assert.assertNull(board);
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
		json.put("name", "TestUserH");
		json.put("email", "emailH@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUserH", userFromDb.getName());
		Assert.assertEquals("emailH@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserI");
		json.put("email", "emailI@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUserI", userFromDb2.getName());
		Assert.assertEquals("emailI@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb2.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);

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
	
	/**
	 * Tests password reset
	 * @throws Exception 
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testResetPassword() throws Exception{
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserQ");
		json.put("email", "emailQ@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUserQ", userFromDb.getName());
		Assert.assertEquals("emailQ@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUserR");
		json.put("email", "emailR@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUserR", userFromDb2.getName());
		Assert.assertEquals("emailR@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		json.put("validationCode", userFromDb2.getLoginData().getValidationCode());
		Boolean success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());
		
		json.remove("validationCode");
		json.put("doReset", true);
		// RESET PASSWORD
		success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertNotNull(userFromDb2.getLoginData().getResetCode());
		json = new JSONObject();
		json.put("resetCode", userFromDb2.getLoginData().getResetCode());
		json.put("password", "asdf");
		success = doPut("http://localhost:8080/user/{0}/logindata", null, json, Boolean.class,
				userFromDb2.getId());
		Assert.assertTrue(success);
		// REQUEST TOKEN
		json.put("email", "emailR@fhnw.ch");
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);
		Assert.assertNotNull(token);
	}
}