package ch.fhnw.wodss.notification;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.integration.AbstractIntegrationTest;
import ch.fhnw.wodss.security.Token;

public class NewBoardNotificationTest extends AbstractIntegrationTest{

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put("email", "hans.muster@fhnw.ch");
		json.put("password", "password");

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/token", null, json, Token.class);

		// CREATE
		User user = UserFactory.getInstance().createUser("Hans", "hans.muster@fhnw.ch");
		Board board = BoardFactory.getInstance().createBoard("TestBoard", user);
		board.addUser(UserFactory.getInstance().createUser("John Rambo", "john.rambo@fhnw.ch"));
		
		JSONParser parser = new JSONParser();
		JSONObject boardjson = (JSONObject) parser.parse(objectMapper.writeValueAsString(board));

		board = doPost("http://localhost:8080/board", token, boardjson, Board.class);
		
		// UPDATE
		board.addUser(UserFactory.getInstance().createUser("Andrew", "andrew.ng@fhnw.ch"));
		boardjson = (JSONObject) parser.parse(objectMapper.writeValueAsString(board));
		doPut("http://localhost:8080/board/{0}", token, boardjson, Board.class, board.getId());

	}
	
}
