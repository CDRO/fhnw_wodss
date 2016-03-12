package ch.fhnw.wodss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.service.BoardService;

@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping(path = "/boards", method = RequestMethod.GET)
	public ResponseEntity<List<Board>> getAllBoards(@RequestHeader(value = "x-session-token") String tokenId,
			@RequestBody Board board) {
		List<Board> boards = boardService.getAll();
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@RequestMapping(path = "/board/{id}", method = RequestMethod.GET)
	public ResponseEntity<Board> getBoard(@RequestHeader(value = "x-session-token") String tokenId,
			@PathVariable Integer id) {
		Board board = boardService.getById(id);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@RequestMapping(path = "/board", method = RequestMethod.POST)
	public ResponseEntity<Board> createBoard(@RequestHeader(value = "x-session-token") String tokenId,
			@RequestBody Board board) {
		Board savedBoard = boardService.saveBoard(board);
		return new ResponseEntity<>(savedBoard, HttpStatus.OK);
	}

	@RequestMapping(path = "/board/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteBoard(@RequestHeader(value = "x-session-token") String tokenId,
			@PathVariable Integer id) {
		boardService.deleteBoard(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@RequestMapping(path = "/board/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Board> updateBoard(@RequestHeader(value = "x-session-token") String tokenId,
			@RequestBody Board board, @PathVariable Integer id) {
		Board updatedBoard = boardService.saveBoard(board);
		return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
	}

}
