package ch.fhnw.wodss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.wrapper.BoardContext;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;

@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping(path = "/boards", method = RequestMethod.GET)
	public ResponseEntity<List<Board>> getAllBoards(@RequestBody BoardContext boardContext) {
		if (TokenHandler.validate(boardContext.getToken().getId())) {
			List<Board> boards = boardService.getAll();
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/board/{id}", method = RequestMethod.GET)
	public ResponseEntity<Board> getBoard(@PathVariable Integer id, @RequestBody BoardContext boardContext) {
		if (TokenHandler.validate(boardContext.getToken().getId())) {
			Board board = boardService.getById(id);
			return new ResponseEntity<>(board, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/board", method = RequestMethod.POST)
	public ResponseEntity<Board> createBoard(@RequestBody BoardContext boardContext) {
		if (TokenHandler.validate(boardContext.getToken().getId())) {
			Board savedBoard = boardService.saveBoard(boardContext.getBoard());
			return new ResponseEntity<>(savedBoard, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/board/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteBoard(@PathVariable Integer id, @RequestBody BoardContext boardContext) {
		if (TokenHandler.validate(boardContext.getToken().getId())) {
			boardService.deleteBoard(id);
			return new ResponseEntity<>("Deleted board.", HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/board/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Board> updateBoard(@PathVariable Integer id, @RequestBody BoardContext boardContext) {
		if (TokenHandler.validate(boardContext.getToken().getId())) {
			Board updatedBoard = boardService.saveBoard(boardContext.getBoard());
			return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

}
