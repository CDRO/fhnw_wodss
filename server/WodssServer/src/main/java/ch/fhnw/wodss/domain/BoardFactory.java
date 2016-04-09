package ch.fhnw.wodss.domain;

/**
 * Responsible for creating boards.
 * 
 * @author tobias
 *
 */
public class BoardFactory {

	private static BoardFactory instance;

	private BoardFactory() {
		super();
	}

	/**
	 * Gets the reference of this object.
	 * 
	 * @return the reference of this object.
	 */
	public static synchronized BoardFactory getInstance() {
		if (instance == null) {
			instance = new BoardFactory();
		}
		return instance;
	}

	/**
	 * Creates a board with the given title.
	 * 
	 * @param title
	 *            the title to set.
	 * @return the board to create.
	 */
	public Board createBoard(String title, User owner) {
		Board board = new Board();
		board.setTitle(title);
		board.setOwner(owner);
		board.addUser(owner);
		return board;
	}

}
