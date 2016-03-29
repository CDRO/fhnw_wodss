package ch.fhnw.wodss.integration;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;

public class BoardIntTest extends AbstractIntegrationTest {

	@Autowired
	private BoardService boardService;
	
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
		Assert.assertEquals(1, board.getId().intValue());
		Board boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard", boardFromDb.getTitle());

		// READ
		board = doGet("http://localhost:8080/board/{0}", token, Board.class, boardFromDb.getId());
		Assert.assertNotNull(board);
		Assert.assertEquals("TestBoard", board.getTitle());
		Assert.assertEquals(1, board.getId().intValue());

		// UPDATE
		json = new JSONObject();
		json.put("id", board.getId());
		json.put("title", "TestBoard2");
		board = doPut("http://localhost:8080/board/{0}", token, json, Board.class, board.getId());
		boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard2", boardFromDb.getTitle());
		Assert.assertEquals("TestBoard2", board.getTitle());
		Assert.assertEquals(1, board.getId().intValue());

		// DELETE
		doDelete("http://localhost:8080/board/{0}", token, Boolean.class, board.getId());
		boardFromDb = boardService.getById(board.getId());
		Assert.assertNull(boardFromDb);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBoardUnauthorizedCRUD() {

		// IVALID TOKEN
		Token token = TokenHandler.register();
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
}