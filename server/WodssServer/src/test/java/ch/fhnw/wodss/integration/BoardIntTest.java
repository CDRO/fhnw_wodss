package ch.fhnw.wodss.integration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

public class BoardIntTest extends AbstractIntegrationTest {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@SuppressWarnings("unchecked")
	@Test
	public void testBoardAuthorizedCRUD() throws Exception {

		JSONObject json = new JSONObject();
		json.put("email", "hans.muster@fhnw.ch");
		json.put("password", "password");

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		// CREATE
		json.clear();
		json.put("title", "TestBoard");

		Board board = doPost("http://localhost:8080/board", token, json, Board.class);
		Assert.assertNotNull(board.getId().intValue());
		Board boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard", boardFromDb.getTitle());

		// READ
		board = doGet("http://localhost:8080/board/{0}", token, Board.class, boardFromDb.getId());
		Assert.assertNotNull(board);
		Assert.assertEquals("TestBoard", board.getTitle());
		Assert.assertNotNull(board.getId().intValue());

		// UPDATE
		board.setTitle("TestBoard2");
		JSONParser parser = new JSONParser();
		Object board2json = parser.parse(objectMapper.writeValueAsString(board));
		board = doPut("http://localhost:8080/board/{0}", token, board2json, Board.class, board.getId());
		boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard2", boardFromDb.getTitle());
		Assert.assertEquals("TestBoard2", board.getTitle());
		Assert.assertNotNull(board.getId().intValue());

		// DELETE
		doDelete("http://localhost:8080/board/{0}", token, Boolean.class, board.getId());
		boardFromDb = boardService.getById(board.getId());
		Assert.assertNull(boardFromDb);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBoardUnauthorizedCRUD() {

		User user = UserFactory.getInstance().createUser("test", "test");

		// INVALID TOKEN
		Token token = TokenHandler.register(user);
		token.setId("asdf-asdf-asdf-asdf");

		JSONObject json = new JSONObject();
		json.put("title", "TestBoard");

		try {
			// CREATE
			doPost("http://localhost:8080/board", token, json, Board.class);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// READ
			doGet("http://localhost:8080/board/{0}", token, Board.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// UPDATE
			doPut("http://localhost:8080/board/{0}", token, json, Board.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// DELETE
			doDelete("http://localhost:8080/board/{0}", token, Boolean.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Tests getting all boards where user is owner or has been invited.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetBoards() throws Exception {
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

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());

		Board[] boards = doGet("http://localhost:8080/boards", token, Board[].class);

		List<Board> boardList = Arrays.asList(boards);

		Assert.assertTrue(boardList.contains(board2));
		Assert.assertTrue(boardList.contains(board3));
	}

	/**
	 * Tests getting the board where user is owner or has been invited. If the
	 * user has not been subscribed to this board, the user in not authorized to
	 * get this board.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetBoard() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser3");
		json.put("email", "email3@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser3", userFromDb.getName());
		Assert.assertEquals("email3@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser4");
		json.put("email", "email4@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser4", userFromDb2.getName());
		Assert.assertEquals("email4@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

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
			board2 = doGet("http://localhost:8080/board/{0}", token, Board.class, board2.getId());
			board3 = doGet("http://localhost:8080/board/{0}", token, Board.class, board3.getId());
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.assertNotNull(board2);
		Assert.assertNotNull(board3);

		try {
			board1 = doGet("http://localhost:8080/board/{0}", token, Board.class, board1.getId());
		} catch (IOException e) {
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * Tests deleting a board. Only the board owner should have the possibility
	 * to delete the board.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteBoard() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser5");
		json.put("email", "email5@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser5", userFromDb.getName());
		Assert.assertEquals("email5@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser6");
		json.put("email", "email6@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser6", userFromDb2.getName());
		Assert.assertEquals("email6@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

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
		board2.addUser(user);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());
		Assert.assertTrue(board2.getUsers().contains(user));

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3.addUser(user);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());
		Assert.assertTrue(board3.getUsers().contains(user));

		boolean delboard2 = false;
		boolean delboard3 = false;
		try {
			delboard2 = doDelete("http://localhost:8080/board/{0}", token, Boolean.class, board2.getId());
			delboard3 = doDelete("http://localhost:8080/board/{0}", token, Boolean.class, board3.getId());
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.assertTrue(delboard2);
		Assert.assertTrue(delboard3);

		boolean delboard1 = false;
		try {
			delboard1 = doDelete("http://localhost:8080/board/{0}", token, Boolean.class, board1.getId());
		} catch (IOException e) {
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.assertFalse(delboard1);

	}
	
	/**
	 * Tests modifying a board. Only the board owner should have the possibility
	 * to modify the board.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testModifyBoard() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser7");
		json.put("email", "email7@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser7", userFromDb.getName());
		Assert.assertEquals("email7@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser8");
		json.put("email", "email8@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser8", userFromDb2.getName());
		Assert.assertEquals("email8@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

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
		board2.addUser(user);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());
		Assert.assertTrue(board2.getUsers().contains(user));

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3.addUser(user);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());
		Assert.assertTrue(board3.getUsers().contains(user));

		board2.setTitle("OtherTitle2");
		board3.setTitle("OtherTitle3");
		
		JSONParser parser = new JSONParser();
		Object board2json = parser.parse(objectMapper.writeValueAsString(board2));
		Object board3json = parser.parse(objectMapper.writeValueAsString(board3));
		
		try {
			board2 = doPut("http://localhost:8080/board/{0}", token, board2json, Board.class, board2.getId());
			board3 = doPut("http://localhost:8080/board/{0}", token, board3json, Board.class, board3.getId());
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.assertEquals("OtherTitle2", board2.getTitle());
		Assert.assertEquals("OtherTitle3", board3.getTitle());

		board3.setTitle("OtherTitle1");
		Object board1json = parser.parse(objectMapper.writeValueAsString(board1));
		try {
			board3 = doPut("http://localhost:8080/board/{0}", token, board1json, Board.class, board1.getId());
		} catch (IOException e) {
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.assertEquals("Board1", board1.getTitle());

	}
}