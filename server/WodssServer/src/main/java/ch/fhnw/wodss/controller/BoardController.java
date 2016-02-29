package ch.fhnw.wodss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }
	
	@RequestMapping(path = "/board/{id}", method = RequestMethod.GET)
	public Board getBoard(@PathVariable Integer id) {
		return boardService.getById(id);
	}
	
	@RequestMapping(path = "/board", method = RequestMethod.POST)
	public void createBoard(@RequestBody Board board) {
		boardService.saveBoard(board);
	}
	
	@RequestMapping(path = "/board/{id}", method = RequestMethod.DELETE)
	public void deleteBoard(@PathVariable Integer id) {
		boardService.deleteBoard(id);
	}
	
	@RequestMapping(path = "/board/{id}", method = RequestMethod.PUT)
	public void updateBoard(@PathVariable Integer id, @RequestBody Board board) {
		boardService.saveBoard(board);
	}
	
	
}
