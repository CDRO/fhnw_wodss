package ch.fhnw.wodss.domain;

import java.util.Date;

/**
 * Responsible for creating tasks.
 * 
 * @author tobias
 *
 */
public class TaskFactory {

	private static TaskFactory instance;

	private TaskFactory() {
		super();
	}

	public static synchronized TaskFactory getInstance() {
		if (instance == null) {
			instance = new TaskFactory();
		}
		return instance;
	}

	/**
	 * Creates a task for the given board with the given description. This task
	 * will be long to the "Unassigned" user.
	 * 
	 * @param board
	 *            The board to set.
	 * @param description
	 *            The description to set.
	 * @return the created task.
	 */
	public Task createTask(Board board, String description) {
		Task task = new Task();
		task.setBoard(board);
		task.setDescription(description);
		task.setCreationDate(new Date());
		task.setState(TaskState.TO_DO);
		return task;
	}

	/**
	 * Creates a task for the given board with the given description and the
	 * given assignee.
	 * 
	 * @param board
	 *            The board to set.
	 * @param description
	 *            The description to set.
	 * @param assignee
	 *            The assignee to set.
	 * @return the created task.
	 */
	public Task createTask(Board board, String description, User assignee) {
		Task task = new Task();
		task.setBoard(board);
		task.setDescription(description);
		task.setAssignee(assignee);
		task.setCreationDate(new Date());
		task.setState(TaskState.TO_DO);
		return task;
	}

}
