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

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.BoardService;

public class BoardIntTest extends AbstractIntegrationTest {

	private RestTemplate restTemplate = new TestRestTemplate();

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private BoardService boardService;

	@Test
	public void testBoardCRUD() throws JsonProcessingException {
		// REQUEST TOKEN
		Token token = restTemplate.postForObject("http://localhost:8080/token", null, Token.class);
		
		// CREATE
		Map<String, Map<String, Object>> requestBody = new HashMap<>();
		Map<String, Object> tokenBody = new HashMap<>();
		Map<String, Object> boardBody = new HashMap<>();
		tokenBody.put("id", token.getId());
		boardBody.put("title", "TestBoard");
		requestBody.put("token", tokenBody);
		requestBody.put("board", boardBody);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<String>(objectMapper.writeValueAsString(requestBody),
				requestHeaders);
		
		Board board = restTemplate.postForObject("http://localhost:8080/board", httpEntity, Board.class);
		Assert.assertNotNull(board.getId());
		Board boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard", boardFromDb.getTitle());
		
		// READ
		board = restTemplate.getForObject("http://localhost:8080/board/{0}", Board.class, board.getId());
		Assert.assertNotNull(board);
		Assert.assertEquals("TestBoard", board.getTitle());
		Board[] boards = restTemplate.getForObject("http://localhost:8080/boards", Board[].class, new Object[0]);
		Assert.assertEquals(1, boards.length);
		
		// UPDATE
		board.setTitle("TestBoard2");
		restTemplate.put("http://localhost:8080/board/{0}", board, board.getId());
		boardFromDb = boardService.getById(board.getId());
		Assert.assertEquals("TestBoard2", boardFromDb.getTitle());
		
		// DELETE
		restTemplate.delete("http://localhost:8080/board/{0}", board.getId());
		boardFromDb = boardService.getById(board.getId());
		Assert.assertNull(boardFromDb);

	}
}