package ch.fhnw.wodss.domain;

import java.util.List;

/**
 * The board that holds tasks.
 * 
 * @author tobias
 *
 */
public interface Board {

	/**
	 * Sets the title of the board.
	 * 
	 * @param title
	 *            the title to set.
	 */
	public void setTitle(String title);

	/**
	 * Gets the title of the board.
	 * 
	 * @return the title of the board.
	 */
	public String getTitle();

	/**
	 * Gets all tasks of this board.
	 * 
	 * @return the list of all tasks.
	 */
	public List<Task> getTasks();

	/**
	 * Adds or saves a task to this board.
	 * 
	 * @param task
	 *            the task to add or to save.
	 */
	public void saveTask(Task task);

	/**
	 * Deletes a task.
	 * 
	 * @param task
	 *            the task to delete.
	 */
	public void deleteTask(Task task);

	/**
	 * Gets all users that have permission to see this board.
	 * 
	 * @return the list of users that have permission to see this board.
	 */
	public List<User> getUsers();

	/**
	 * Permits a user for this board.
	 * 
	 * @param user
	 *            the user to give permission for this board.
	 */
	public void addUser(User user);

	/**
	 * Deletes a user from this board.
	 * 
	 * @param user
	 *            the user to delete.
	 */
	public void deleteUser(User user);

}
