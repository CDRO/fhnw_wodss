package ch.fhnw.wodss.domain;

import java.util.Date;

/**
 * The task to do.
 * 
 * @author tobias
 *
 */
public interface Task {

	/**
	 * Gets the board of this task.
	 * 
	 * @return the board of this task.
	 */
	public Board getBoard();

	/**
	 * Sets the board of this task.
	 * 
	 * @param board
	 *            the board to set.
	 */
	public void setBoard(Board board);

	/**
	 * Gets the state of the current task.
	 * 
	 * @return the state of the current task.
	 */
	public TaskState getState();

	/**
	 * Sets the state of the current task.
	 * 
	 * @param the state to set.
	 */
	public void setState(TaskState state);

	/**
	 * Gets the assignee of this task.
	 * 
	 * @return the assignee of this task.
	 */
	public User getAssignee();

	/**
	 * Sets the assignee of this task.
	 * 
	 * @param user
	 *            the assignee to set.
	 */
	public void setAssignee(User assignee);

	/**
	 * Gets the creation date of this task.
	 * 
	 * @return the creation date of this task.
	 */
	public Date getCreationDate();

	/**
	 * Sets the creation date of this task.
	 * 
	 * @param date
	 *            the creation date to set.
	 */
	public void setCreationDate(Date creationDate);

	/**
	 * Gets the due date of this task.
	 * 
	 * @return the due date of this task.
	 */
	public Date getDueDate();

	/**
	 * Sets the due date of this task.
	 * 
	 * @param date
	 *            the due date to set.
	 */
	public void setDueDate(Date dueDate);

	/**
	 * Gets the date when this task has been completed.
	 * 
	 * @return the date when this task has been completed.
	 */
	public Date getDoneDate();

	/**
	 * Sets the date when this task has been completed.
	 * 
	 * @param the date to set when this task has been completed.
	 */
	public void setDoneDate(Date doneDate);

	/**
	 * Gets the description of this task.
	 * 
	 * @return the description of this task.
	 */
	public String getDescription();

	/**
	 * Sets the description of this task.
	 * 
	 * @param description
	 *            the description to set.
	 */
	public void setDescription(String description);

}
