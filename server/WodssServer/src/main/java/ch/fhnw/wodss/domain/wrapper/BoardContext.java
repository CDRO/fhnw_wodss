package ch.fhnw.wodss.domain.wrapper;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.security.Token;

public class BoardContext {

	private Token token;
	private Board board;
	
	public BoardContext(){
		super();
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}
	
	
	
}
