package ch.fhnw.wodss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	/**
	 * Get all boards a user is owner or subscribed to.
	 * 
	 * @param token
	 *            the security token to check whether user is logged in.
	 * @return the boards.
	 */
	@RequestMapping(path = "/boards", method = RequestMethod.GET)
	public ResponseEntity<List<Board>> getAllBoards(@RequestHeader(value = "x-session-token") Token token) {
		User user = TokenHandler.getUser(token.getId());
		// reload from Database
		user = userService.getById(user.getId());
		return new ResponseEntity<>(user.getBoards(), HttpStatus.OK);
	}

	/**
	 * Get the board specified by id if the user is the owner of this board, or
	 * has subscribed for this board.
	 * 
	 * @param token
	 *            the security token to check whether user is logged in.
	 * @param id
	 *            the board id that should be get.
	 * @return the board.
	 */
	@RequestMapping(path = "/board/{id}", method = RequestMethod.GET)
	public ResponseEntity<Board> getBoard(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		User user = TokenHandler.getUser(token.getId());
		// reload from Database
		user = userService.getById(user.getId());
		Board board = boardService.getById(id);
		if (user.getBoards().contains(board)) {
			return new ResponseEntity<>(board, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Creates a new board.
	 * 
	 * @param token
	 *            the security token to check whether user is logged in.
	 * @param board
	 *            the board that should be created.
	 * @return the created board, with id not null.
	 */
	@RequestMapping(path = "/board", method = RequestMethod.POST)
	public ResponseEntity<Board> createBoard(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody Board board) {
		User user = TokenHandler.getUser(token.getId());
		board.setOwner(user);
		board.addUser(user);
		Board savedBoard = boardService.saveBoard(board);
		return new ResponseEntity<>(savedBoard, HttpStatus.OK);
	}

	/**
	 * Deletes a board, but only if the user who wants to delete it, is the
	 * owner of the board.
	 * 
	 * @param token
	 *            the security token to check whether the user is logged in.
	 * @param id
	 *            the board id to delete.
	 * @return <code>true</code> if board is deleted <code>false</code>
	 *         otherwise
	 */
	@RequestMapping(path = "/board/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteBoard(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		User user = TokenHandler.getUser(token.getId());
		// reload from Database
		user = userService.getById(user.getId());
		Board board = boardService.getById(id);
		if (user.equals(board.getOwner())) {
			boardService.deleteBoard(id);
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Modifies an existing board, but only if the user who wants to modify it,
	 * is the user of the board.
	 * 
	 * @param token
	 *            the security token to check whether the user is logged in.
	 * @param board
	 *            the modified board that should be saved to database.
	 * @param id
	 *            the board id that should be modified.
	 * @return the saved modified board.
	 */
	@RequestMapping(path = "/board/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Board> updateBoard(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody Board board, @PathVariable Integer id) {
		User user = TokenHandler.getUser(token.getId());
		// reload from Database
		user = userService.getById(user.getId());
		Board aCurrBoard = boardService.getById(id);
		if (user.equals(aCurrBoard.getOwner())) {
			Board updatedBoard = boardService.saveBoard(board);
			return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
